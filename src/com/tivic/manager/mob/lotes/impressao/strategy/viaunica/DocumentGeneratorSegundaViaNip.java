package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.awt.Image;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.banco.IBancoService;
import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitimagem.IAitImagemService;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.colaborador.IColaboradorService;
import com.tivic.manager.mob.lotes.builders.impressao.viaunica.ReportDadosNotificacaoBuilder;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lotes.factory.impressao.TipoRemessaDocumentFactory;
import com.tivic.manager.mob.lotes.impressao.ait.GeraDadosNotificacaoAit;
import com.tivic.manager.mob.lotes.impressao.ait.GeraDadosNotificacaoAitNic;
import com.tivic.manager.mob.lotes.impressao.codigobarras.CodigoBarras;
import com.tivic.manager.mob.lotes.impressao.codigobarras.GerarCodigoBarrasFactory;
import com.tivic.manager.mob.lotes.impressao.pix.AutenticacaoUsuarioPix;
import com.tivic.manager.mob.lotes.impressao.pix.DadosAutenticacaoEnvio;
import com.tivic.manager.mob.lotes.impressao.pix.DadosAutenticacaoRetorno;
import com.tivic.manager.mob.lotes.impressao.pix.ImagemQrCodePixGenerator;
import com.tivic.manager.mob.lotes.impressao.pix.builders.DadosAutenticacaoEnvioBuilder;
import com.tivic.manager.mob.lotes.impressao.strategy.NomeTipoImpressaoNipStrategy;
import com.tivic.manager.mob.lotes.impressao.strategy.documento.IGeraSegundaViaImpressao;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class DocumentGeneratorSegundaViaNip implements IGeraSegundaViaImpressao{
	
	private CustomConnection customConnection;
	private IBancoService bancoService;
	private IAitMovimentoService aitMovimentoService;
	private IParametroService parametroService;
	private AitRepository aitRepository;
	private Image imgMulta;
	private AitImagem aitImagem;
	private IAitImagemService aitImagemService;
	private IParametroRepository parametroRepository;
	private IColaboradorService colaboradorService;
	
	public DocumentGeneratorSegundaViaNip() throws Exception {
		bancoService = (IBancoService) BeansFactory.get(IBancoService.class); 
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitImagemService = (IAitImagemService) BeansFactory.get(IAitImagemService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		colaboradorService = (IColaboradorService) BeansFactory.get(IColaboradorService.class);
	}
	
	@Override
	public byte[] gerarDocumentos(int cdAit, Boolean printPortal, CustomConnection customConnection) throws Exception, ValidacaoException {
		this.customConnection = customConnection;
		ReportDadosNotificacaoBuilder reportBuilder = new ReportDadosNotificacaoBuilder(customConnection);
	    reportBuilder.addParameter("PRINT_PORTAL", printPortal);
	    ReportCriterios reportCriterios = reportBuilder.getParametros();
	    adicionarParametrosEspecificosNip(reportBuilder);
		return findDocumentoNip(cdAit, reportCriterios, reportBuilder);
	}
	
	private void adicionarParametrosEspecificosNip(ReportDadosNotificacaoBuilder reportBuilder) throws Exception {
	    List<String> criterios = Arrays.asList(
			"MOB_IMPRESSOS_DS_TEXTO_NIP",
	        "NR_BANCO_CREDITO",
	        "NR_AGENCIA_CREDITO",
	        "NR_CONTA_CREDITO",
	        "MOB_PRAZOS_NR_RECURSO_JARI",
	        "MOB_NOME_COORDENADOR_IMPRESSAO",
	        "MOB_INFORMACOES_ADICIONAIS_NIP",
	        "NR_CD_FEBRABAN",
	        "MOB_CD_ORGAO_AUTUADOR"
	    );
	    reportBuilder.addParameters(criterios, customConnection);
	}
	
	private byte[] findDocumentoNip(int cdAit, ReportCriterios reportCriterios, ReportDadosNotificacaoBuilder reportBuilder) throws Exception {
		Notificacao notificacao = verificarAitNic(cdAit)
				? new GeraDadosNotificacaoAitNic().getNotificacaoByCdAit(cdAit) 
				: new GeraDadosNotificacaoAit().getNotificacaoByCdAit(cdAit);
		montarDadosDocumento(notificacao, reportCriterios);
		List<Notificacao> dadosSubreport = Arrays.asList(notificacao);
		reportBuilder.setParamsSubreport(dadosSubreport);
		Report report = new ReportBuilder()
				.list(Arrays.asList(notificacao))
				.reportCriterios(reportCriterios)
				.build();
		return report.getReportPdf(new NomeTipoImpressaoNipStrategy().buscar(getMovimentoPenalidade(cdAit), TipoRemessaCorreiosEnum.CARTA_SIMPLES));
	}
	
	private void montarDadosDocumento(Notificacao dadosNotificacao, ReportCriterios reportCriterios) throws Exception {
		TipoRemessaDocumentFactory tipoRemessaFactory = new TipoRemessaDocumentFactory();
		tipoRemessaFactory.addBarCodeCep(dadosNotificacao);
		if(dadosNotificacao.getCdEquipamento() > 0) {
			dadosNotificacao.setNomeEquipamento(EquipamentoEnum.valueOf(dadosNotificacao.getTpEquipamento()));
		} else {
			dadosNotificacao.setNomeEquipamento(EquipamentoEnum.NENHUM.getValue());
		}
		dadosNotificacao.setVlMultaComDesconto((dadosNotificacao.getVlMulta() - (Double.valueOf(20) * dadosNotificacao.getVlMulta()) / 100));
		dadosNotificacao.setBancosConveniados(bancoService.pegarBancoConveniado());
		trataDefesaPrevia(dadosNotificacao);
		dadosNotificacao.setDtEmissao(Util.getDataAtual());
		boolean orgaoOptanteRecebimentoViaPix = this.parametroRepository.getValorOfParametroAsBoolean("LG_PIX");
		String tokenAutenticacao = null;
		boolean isPaga = verificarAitComMultaPaga(dadosNotificacao.getCdAit());
		if (reportCriterios.getParametros().get("NR_CD_FEBRABAN") != null && !isPaga) {
			if(orgaoOptanteRecebimentoViaPix) {
				tokenAutenticacao = obterTokenPix();
			}
			CodigoBarras codigoBarras = new GerarCodigoBarrasFactory()
					.getStrategy()
					.gerarCodigoBarras(dadosNotificacao, reportCriterios);
				dadosNotificacao.setLinhaDigitavel(codigoBarras.getLinhaDigitavel());
    		if(orgaoOptanteRecebimentoViaPix && tokenAutenticacao != null) {
		        ImagemQrCodePixGenerator qrCodeGenerator = new ImagemQrCodePixGenerator();
		        qrCodeGenerator.registrarArrecadacaoPix(dadosNotificacao, codigoBarras.getCodigoBarrasComDv(), tokenAutenticacao);
			}
			reportCriterios.addParametros("BARRAS", codigoBarras.getBarcodeImage() != null ? codigoBarras.getBarcodeImage() : null);
		} 
		reportCriterios.addParametros("TXT_MULTA_PAGA", isPaga ? "MULTA JÁ PAGA" : " ");
		ApresentacaoCondutor apresentacaoCondutor = getCondutorFici(dadosNotificacao.getCdAit());
		if (apresentacaoCondutor != null) {
			dadosNotificacao.setNmCondutor(apresentacaoCondutor.getNmCondutor() != null ? apresentacaoCondutor.getNmCondutor() : null);
			dadosNotificacao.setNrCnhCondutor(apresentacaoCondutor.getNrCnh() != null ? apresentacaoCondutor.getNrCnh() : null);
	        dadosNotificacao.setUfCnhCondutor(apresentacaoCondutor.getUfCnh() != null ? apresentacaoCondutor.getUfCnh() : null);
	        dadosNotificacao.setNrCpfCondutor(apresentacaoCondutor.getNrCpfCnpj() != null ? apresentacaoCondutor.getNrCpfCnpj() : null);
	    }
    	int tpPenalidade = getMovimentoPenalidade(dadosNotificacao.getCdAit());
    	Map<String, Object> assinaturaAutoridade = colaboradorService.buscarAssinaturaAutoridade();
		reportCriterios.addParametros("MOB_IMAGEM_VEICULO", pegarImagemVeiculo(dadosNotificacao.getCdAit()));
		reportCriterios.addParametros("LG_GERACAO_VIA_UNICA", true);
		reportCriterios.addParametros("MOB_TP_DOCUMENTO", TipoStatusEnum.NIP_ENVIADA.getKey());
		reportCriterios.addParametros("IS_SEGUNDA_VIA", true);
		reportCriterios.addParametros("TP_PENALIDADE", tpPenalidade);
		reportCriterios.addParametros("ASSINATURA_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
				? (byte[]) assinaturaAutoridade.get("assinaturaAutoridade") : null);
		reportCriterios.addParametros("NM_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
				? (String) assinaturaAutoridade.get("nomeAutoridade") : null);
	}
	
	private String obterTokenPix() {
	    try {
	        String email = ManagerConf.getInstance().get("EMAIL_AUTENTICACAO_PIX");
	        String senha = ManagerConf.getInstance().get("SENHA_AUTENTICACAO_PIX");
	        DadosAutenticacaoEnvio dadosEnvio = new DadosAutenticacaoEnvioBuilder()
	            .addEmail(email)
	            .addSenha(senha)
	            .build();
	        AutenticacaoUsuarioPix registro = new AutenticacaoUsuarioPix(dadosEnvio);
	        DadosAutenticacaoRetorno dadosRetorno = registro.autenticar();

	        return dadosRetorno.getToken();
	    } catch (Exception e) {
	        System.err.println("Erro ao obter token Pix: " + e.getMessage());
	        return null; 
	    }
	}
	
	private Image pegarImagemVeiculo(int cdAit) throws Exception {
		this.imgMulta = null;
		this.aitImagem = new AitImagem();
		this.aitImagem = aitImagemService.getFromAit(cdAit);
		if(aitImagem.getBlbImagem() != null ) {
			this.imgMulta = new ImageIcon(aitImagem.getBlbImagem()).getImage();
		}
		return this.imgMulta;
	}
	
	private boolean verificarAitComMultaPaga(int cdAit) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.MULTA_PAGA.getKey(), true);
		List<AitMovimento> aitMovimento = this.aitMovimentoService.find(searchCriterios);
		return !aitMovimento.isEmpty();
	}
	
	private void trataDefesaPrevia(Notificacao dadosNotificacao) throws Exception {
		AitMovimento movimentoDefesaIndeferida = verificarDefesaIndeferida(dadosNotificacao.getCdAit());
		if (movimentoDefesaIndeferida.getNrProcesso() != null) {
			String defesaPreviaIndeferida = ParametroServices.getValorOfParametroAsString("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", "");
			String coodernador = ParametroServices.getValorOfParametroAsString("MOB_NOME_COORDENADOR_IMPRESSAO", "");
			String cidade = ParametroServices.getValorOfParametroAsString("NM_CIDADE", "");
			String proprietario =  dadosNotificacao.getNmProprietario() != null ? dadosNotificacao.getNmProprietario() : " ";
			String nrAit = dadosNotificacao.getIdAit() != null ? dadosNotificacao.getIdAit() : " ";
			String nrRenavan = dadosNotificacao.getNrRenavan() != null ? dadosNotificacao.getNrRenavan() : " ";
			String nrPlaca = dadosNotificacao.getNrPlaca() != null ? dadosNotificacao.getNrPlaca() : " ";
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#PROPRIETARIO>", proprietario);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr PROCESSO>", movimentoDefesaIndeferida.getNrProcesso());
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#COORDENADOR>", coodernador);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr AIT>", nrAit);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr RENAVAN>", nrRenavan);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr PLACA>", nrPlaca);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#CIDADE>", cidade);
			dadosNotificacao.setDefesaPreviaIndeferida(defesaPreviaIndeferida);
		}
	}
	
	private AitMovimento verificarDefesaIndeferida(int cdAit) throws Exception {
		List<AitMovimento> listDefesas = aitMovimentoService.getAllDefesas(cdAit);
		for(AitMovimento movimentoDefesaIndeferida : listDefesas) {
			if(movimentoDefesaIndeferida.getTpStatus() == TipoStatusEnum.DEFESA_INDEFERIDA.getKey() ||
				movimentoDefesaIndeferida.getTpStatus() == TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey()) {
				return movimentoDefesaIndeferida;
			}
		}
		return new AitMovimento();
	}
	
	private ApresentacaoCondutor getCondutorFici(int cdAit) throws Exception {
		int codigoFiciAceita = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_APRESENTACAO_CONDUTOR_ACEITO",	0);
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		searchCriterios.addCriteriosEqualInteger("A.cd_ocorrencia", codigoFiciAceita, true);
		Search<ApresentacaoCondutor> searchCondutor = new SearchBuilder<ApresentacaoCondutor>("mob_ait_movimento A")
				.fields("D.nm_condutor, D.nr_cnh, D.uf_cnh, D.nr_cpf_cnpj").addJoinTable("JOIN mob_ait_movimento_documento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN ptc_documento C ON (B.cd_documento = C.cd_documento)")
				.addJoinTable("JOIN ptc_apresentacao_condutor D ON (C.cd_documento = D.cd_documento)")
				.additionalCriterias("C.cd_fase <> " + parametroService.getValorOfParametroAsInt("CD_SITUACAO_DOCUMENTO_CANCELADO").getNrValorParametro())
				.searchCriterios(searchCriterios)
			.build();
		List<ApresentacaoCondutor> apresentacaoCondutorList = searchCondutor.getList(ApresentacaoCondutor.class);
	    if (!apresentacaoCondutorList.isEmpty()) {
	        return apresentacaoCondutorList.get(0);
	    }
	    return null;
	}
	
	private boolean verificarAitNic(int cdAit) throws Exception {
		Ait ait = this.aitRepository.get(cdAit);
		return ait != null && ait.getCdAitOrigem() > 0;
	}
	
	private int getMovimentoPenalidade(int cdAit) throws Exception, ValidacaoException {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		searchCriterios.addCriterios("tp_status", String.valueOf(TipoStatusEnum.NIP_ENVIADA.getKey()) + ", " +
	            String.valueOf(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey()) + ", " +
	            String.valueOf(TipoStatusEnum.NIC_ENVIADA.getKey()), ItemComparator.IN, Types.INTEGER);
		List<AitMovimento> aitMovimentoList = this.aitMovimentoService.find(searchCriterios, new CustomConnection());
		if(aitMovimentoList.isEmpty()) {
	        throw new ValidacaoException("Movimento de penalidade não localizado ou não registrado.");
		}
		for (AitMovimento movimento : aitMovimentoList) {
	        if (movimento.getTpStatus() == TipoStatusEnum.NIC_ENVIADA.getKey()) {
	            return TipoStatusEnum.NIC_ENVIADA.getKey();
	        }
	    }
	    return aitMovimentoList.get(0).getTpStatus();
	}

}
