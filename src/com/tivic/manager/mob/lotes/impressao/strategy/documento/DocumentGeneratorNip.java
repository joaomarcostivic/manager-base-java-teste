package com.tivic.manager.mob.lotes.impressao.strategy.documento;

import java.awt.Image;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitimagem.IAitImagemService;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.colaborador.IColaboradorService;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lotes.dto.DadosDocumento;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lotes.factory.impressao.TipoRemessaFactory;
import com.tivic.manager.mob.lotes.impressao.codigobarras.CodigoBarras;
import com.tivic.manager.mob.lotes.impressao.codigobarras.GerarCodigoBarrasFactory;
import com.tivic.manager.mob.lotes.impressao.pix.AutenticacaoUsuarioPix;
import com.tivic.manager.mob.lotes.impressao.pix.DadosAutenticacaoEnvio;
import com.tivic.manager.mob.lotes.impressao.pix.DadosAutenticacaoRetorno;
import com.tivic.manager.mob.lotes.impressao.pix.ImagemQrCodePixGenerator;
import com.tivic.manager.mob.lotes.impressao.pix.builders.DadosAutenticacaoEnvioBuilder;
import com.tivic.manager.mob.lotes.impressao.strategy.NomeTipoImpressaoNipStrategy;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.banco.BancoRepository;
import com.tivic.manager.mob.lotes.repository.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.mob.lotes.service.impressao.facades.DocumentGeneratorFacade;
import com.tivic.manager.mob.lotes.utils.impressao.HandleDefesaPrevia;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class DocumentGeneratorNip implements IDocumentGeneratorStrategy {
	private BancoRepository bancoRepository;
	private TipoRemessaCorreiosEnum tpRemessa;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private LoteRepository loteRepository;
	private IArquivoService arquivoService;
	private IAitImagemService aitImagemService;
	private CustomConnection customConnection;
	private IAitMovimentoService aitMovimentoService;
	private AitMovimentoRepository aitMovimentoRepository;
	private ILoteImpressaoService loteImpressaoService;
	private IParametroRepository parametroRepository;
	private IColaboradorService colaboradorService;
	
	public DocumentGeneratorNip(TipoRemessaCorreiosEnum tpRemessa) throws Exception {
		this.tpRemessa = tpRemessa;
		bancoRepository = (BancoRepository) BeansFactory.get(BancoRepository.class);
		loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		aitImagemService = (IAitImagemService) BeansFactory.get(IAitImagemService.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		colaboradorService = (IColaboradorService) BeansFactory.get(IColaboradorService.class);
	}
	
	@Override
	public byte[] generate(LoteImpressao loteImpressao, String idLote, CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		List<String> criterios = new ArrayList<>(Arrays.asList(
            "MOB_IMPRESSOS_DS_TEXTO_NIP",
            "NR_BANCO_CREDITO",
            "NR_AGENCIA_CREDITO",
            "NR_CONTA_CREDITO",
            "MOB_PRAZOS_NR_RECURSO_JARI",
            "MOB_NOME_COORDENADOR_IMPRESSAO",
            "MOB_INFORMACOES_ADICIONAIS_NIP",
            "NR_CD_FEBRABAN",
            "MOB_CD_ORGAO_AUTUADOR"
		));
		
		DadosDocumento dadosDocumento = TipoRemessaFactory.getStrategy(this.tpRemessa)
	    		.getDadosDocumento(loteImpressao, customConnection)
	    		.addParameters(criterios, customConnection)
    			.setArquivoListaPostagem(customConnection)
	    		.criar();
	    
	    if (dadosDocumento.getNotificacoes().size() >= 2) {
    		dadosDocumento.getCriterios().addParametros("IS_NOT_SEGUNDA_VIA", true);
    	}

	    boolean orgaoOptanteRecebimentoViaPix = this.parametroRepository.getValorOfParametroAsBoolean("LG_PIX");
	    String tokenAutenticacao = null;
	    DocumentGeneratorFacade documentGeneratorFacade = new DocumentGeneratorFacade(loteImpressao);
	    for(Notificacao notificacao : dadosDocumento.getNotificacoes()) {
			boolean isPaga = verificarAitComMultaPaga(notificacao.getCdAit());
			notificacao.setVlMultaComDesconto((Math.round(
				    (notificacao.getVlMulta() - (Double.valueOf(20) * notificacao.getVlMulta()) / 100) * 100.0
				) / 100.0));
	    	notificacao.setBancosConveniados(bancoRepository.getConveniados());
	    	HandleDefesaPrevia.handle(notificacao);
	    	if(!isPaga) {
	    		CodigoBarras codigoBarras = new GerarCodigoBarrasFactory()
	    				.getStrategy()
	    				.gerarCodigoBarras(notificacao, dadosDocumento.getCriterios());
	    		notificacao.setLinhaDigitavel(codigoBarras.getLinhaDigitavel());
	    		if(orgaoOptanteRecebimentoViaPix) {
	    			tokenAutenticacao = obterTokenPix();
	    		}
	    		if(orgaoOptanteRecebimentoViaPix && tokenAutenticacao != null) {
			        ImagemQrCodePixGenerator qrCodeGenerator = new ImagemQrCodePixGenerator();
			        qrCodeGenerator.registrarArrecadacaoPix(notificacao, codigoBarras.getCodigoBarrasComDv(), tokenAutenticacao);
	    		}
	    		dadosDocumento.getCriterios().addParametros("BARRAS", codigoBarras.getBarcodeImage() != null ? codigoBarras.getBarcodeImage() : null);
			} 
	    	dadosDocumento.getCriterios().addParametros("TXT_MULTA_PAGA", isPaga ? "MULTA JÁ PAGA" : " ");
	    	int tpPenalidade = getTipoStatusNotificacao(notificacao.getCdAit(), notificacao.getIdAitGeradora());
	    	String nmDocumentoImpressao = new NomeTipoImpressaoNipStrategy().buscar(tpPenalidade, this.tpRemessa);
	    	Map<String, Object> assinaturaAutoridade = colaboradorService.buscarAssinaturaAutoridade();
	    	dadosDocumento.setNmDocumento(nmDocumentoImpressao);
	    	dadosDocumento.getCriterios().addParametros("MOB_IMAGEM_VEICULO", capturarImagemVeiculo(notificacao.getCdAit()));
    		dadosDocumento.getCriterios().addParametros("NM_REMESSA_CORREIOS", this.tpRemessa.getValue());
    		dadosDocumento.getCriterios().addParametros("LG_GERACAO_VIA_UNICA",  Boolean.TRUE.equals(loteImpressao.getLgGeracaoViaUnica()));
    		dadosDocumento.getCriterios().addParametros("MOB_TP_DOCUMENTO", TipoStatusEnum.NIP_ENVIADA.getKey());
    		dadosDocumento.getCriterios().addParametros("TP_PENALIDADE", tpPenalidade);
    		dadosDocumento.getCriterios().addParametros("TP_MODELO_NOTIFICACAO", Integer.parseInt(ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nip")));
    		dadosDocumento.getCriterios().addParametros("ASSINATURA_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
    				? (byte[]) assinaturaAutoridade.get("assinaturaAutoridade") : null);
    		dadosDocumento.getCriterios().addParametros("NM_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
    				? (String) assinaturaAutoridade.get("nomeAutoridade") : null);
    		preencherDadosCondutor(notificacao);
    		documentGeneratorFacade.generateNotificacao(notificacao, dadosDocumento.getCriterios(), nmDocumentoImpressao, this.tpRemessa);
	    	salvarDocumentoLoteAit(notificacao.getCdLoteImpressao(), notificacao.getCdAit(), customConnection);
	    }
	    
	    documentGeneratorFacade.updateStatusEtiquetas(TipoStatusEnum.NIP_ENVIADA, dadosDocumento.getEtiquetas(), customConnection);
        byte[] pdfBytes = documentGeneratorFacade.gerarDocumentoLote(idLote);
		
		salvarDocumentoLote(loteImpressao, pdfBytes);
	        return pdfBytes;
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

	private void salvarDocumentoLoteAit(int cdLoteImpressao, int cdAit, CustomConnection customConnection) throws Exception {
		LoteImpressaoAit loteImpressaoAit = this.loteImpressaoAitRepository.get(cdLoteImpressao, cdAit, customConnection);
		loteImpressaoAit.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO);
		this.loteImpressaoAitRepository.update(loteImpressaoAit, customConnection);
	}
	
	private void salvarDocumentoLote(LoteImpressao loteImpressao, byte[] bytePdfNotificacao) throws Exception {
		Lote lote = this.loteRepository.get(loteImpressao.getCdLote(), new CustomConnection());
		Arquivo arquivoNotificacao = new ArquivoBuilder()
				.setBlbArquivo(bytePdfNotificacao)
				.setCdUsuario(lote.getCdCriador())
				.setNmArquivo("LOTE_NOTIFICACAO_NP_" + loteImpressao.getCdLoteImpressao() + ".pdf" )
				.setNmDocumento("Lote de Notificações NP")
				.setDtArquivamento(DateUtil.getDataAtual())
				.setDtCriacao(DateUtil.getDataAtual())
				.build();
		this.arquivoService.save(arquivoNotificacao, this.customConnection);
		lote.setCdArquivo(arquivoNotificacao.getCdArquivo());
		loteImpressao.setStLote(LoteImpressaoSituacao.EM_IMPRESSAO);
		this.loteImpressaoService.save(loteImpressao, customConnection);
		this.loteRepository.update(lote, this.customConnection);
	}
	
	private Image capturarImagemVeiculo(int cdAit) throws Exception {
		Image imgMulta = null;
		AitImagem aitImagem = new AitImagem();
		aitImagem = aitImagemService.getFromAit(cdAit);
		if(aitImagem.getBlbImagem() != null ) {
			imgMulta = new ImageIcon(aitImagem.getBlbImagem()).getImage();
		}
		return imgMulta;
	}
	
	private boolean verificarAitComMultaPaga(int cdAit) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.MULTA_PAGA.getKey(), true);
		List<AitMovimento> aitMovimento = this.aitMovimentoService.find(searchCriterios);
		if(aitMovimento.isEmpty()) {
			return false;
		}
		return true;
	}
	
	private int getTipoStatusNotificacao(int cdAit, String idAitGeradora) throws Exception, ValidacaoException {
		if (idAitGeradora != null) {
		        return TipoStatusEnum.NIC_ENVIADA.getKey();
		}
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		searchCriterios.addCriterios("tp_status", String.valueOf(TipoStatusEnum.NIP_ENVIADA.getKey()) + ", " +
	            String.valueOf(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey()), ItemComparator.IN, Types.INTEGER);
		List<AitMovimento> aitMovimentoList = this.aitMovimentoRepository.find(searchCriterios, new CustomConnection());
	    if (aitMovimentoList.isEmpty()) {
	        throw new ValidacaoException("Nenhum movimento encontrado para o AIT.");
	    }
	    return aitMovimentoList.get(0).getTpStatus();
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
				.additionalCriterias("C.cd_fase <> " + parametroRepository.getValorOfParametroAsInt("CD_SITUACAO_DOCUMENTO_CANCELADO"))
				.searchCriterios(searchCriterios)
			.build();
		List<ApresentacaoCondutor> apresentacaoCondutorList = searchCondutor.getList(ApresentacaoCondutor.class);
	    if (!apresentacaoCondutorList.isEmpty()) {
	        return apresentacaoCondutorList.get(0);
	    }
	    return null;
	}
	
	private void preencherDadosCondutor(Notificacao notificacao) throws Exception {
		ApresentacaoCondutor apresentacaoCondutor = getCondutorFici(notificacao.getCdAit());
		if (apresentacaoCondutor != null) {
			notificacao.setNmCondutor(apresentacaoCondutor.getNmCondutor() != null ? apresentacaoCondutor.getNmCondutor() : null);
			notificacao.setNrCnhCondutor(apresentacaoCondutor.getNrCnh() != null ? apresentacaoCondutor.getNrCnh() : null);
			notificacao.setUfCnhCondutor(apresentacaoCondutor.getUfCnh() != null ? apresentacaoCondutor.getUfCnh() : null);
			notificacao.setNrCpfCondutor(apresentacaoCondutor.getNrCpfCnpj() != null ? apresentacaoCondutor.getNrCpfCnpj() : null);
		}
	}

}
