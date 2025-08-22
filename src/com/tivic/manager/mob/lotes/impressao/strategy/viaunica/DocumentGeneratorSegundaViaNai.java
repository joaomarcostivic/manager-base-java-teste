package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitReportServices;
import com.tivic.manager.mob.AitReportValidatorsNAI;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitimagem.IAitImagemService;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.colaborador.IColaboradorService;
import com.tivic.manager.mob.infracao.TipoResponsabilidadeInfracaoEnum;
import com.tivic.manager.mob.lotes.builders.impressao.viaunica.ReportDadosNotificacaoBuilder;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.mob.lotes.factory.impressao.TipoRemessaDocumentFactory;
import com.tivic.manager.mob.lotes.impressao.ait.GeraDadosNotificacaoAit;
import com.tivic.manager.mob.lotes.impressao.ait.GeraDadosNotificacaoAitNic;
import com.tivic.manager.mob.lotes.impressao.prazorecurso.PrazoRecursoStrategy;
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

public class DocumentGeneratorSegundaViaNai implements IGeraSegundaViaImpressao {
	
	private CustomConnection customConnection;	
	private IAitImagemService aitImagemService;
	private byte[] imgMulta;
	private AitImagem aitImagem;
	private IAitMovimentoService aitMovimentoServices;
	private IParametroService parametroService;
	private AitRepository aitRepository;
	private IColaboradorService colaboradorService;
	
	public DocumentGeneratorSegundaViaNai()throws Exception{
		this.aitImagemService = (IAitImagemService) BeansFactory.get(IAitImagemService.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		colaboradorService = (IColaboradorService) BeansFactory.get(IColaboradorService.class);
	}
	
	@Override
	public byte[] gerarDocumentos(int cdAit, Boolean printPortal, CustomConnection customConnection) throws Exception {
	    this.customConnection = customConnection;
	    ReportDadosNotificacaoBuilder reportBuilder = new ReportDadosNotificacaoBuilder(customConnection);
	    reportBuilder.addParameter("PRINT_PORTAL", printPortal);
	    ReportCriterios reportCriterios = reportBuilder.getParametros();
	    adicionarParametrosEspecificosNai(reportBuilder);

	    return findDocumentoNai(cdAit, reportCriterios, reportBuilder);
	}

	private void adicionarParametrosEspecificosNai(ReportDadosNotificacaoBuilder reportBuilder) throws Exception {
	    List<String> criterios = Arrays.asList(
	        "MOB_CD_ORGAO_AUTUADOR",
	        "MOB_IMPRESSOS_DS_TEXTO_NAI",
	        "DS_ENDERECO",
	        "NM_SUBORGAO",
	        "mob_impressao_tp_modelo_nai",
	        "mob_impressao_tp_modelo_nip",
	        "MOB_APRESENTACAO_CONDUTOR",
	        "MOB_PUBLICAR_AITS_NAO_ENTREGUES",
	        "mob_apresentar_observacao",
	        "MOB_CORREIOS_SG_CLIENTE"
	    );
	    reportBuilder.addParameters(criterios, customConnection);
	}
	
	private byte[] findDocumentoNai(int cdAit, ReportCriterios reportCriterios, ReportDadosNotificacaoBuilder reportBuilder) throws Exception {
	    Boolean isMultaNic = verificarAitNic(cdAit);
	    Notificacao notificacao = isMultaNic
	            ? new GeraDadosNotificacaoAitNic().getNotificacaoByCdAit(cdAit)
	            : new GeraDadosNotificacaoAit().getNotificacaoByCdAit(cdAit);
	    montarDadosDocumento(notificacao, reportCriterios);
	    List<Notificacao> dadosSubreport = Arrays.asList(notificacao);
	    reportBuilder.setParamsSubreport(dadosSubreport);
	    Report report = new ReportBuilder()
	            .list(Arrays.asList(notificacao))
	            .reportCriterios(reportCriterios)
	            .build();

	    return report.getReportPdf(isMultaNic ? "mob/nic_na_np_generico" : "mob/na_carta_simples");
	}
	
	private void montarDadosDocumento(Notificacao dadosNotificacao, ReportCriterios reportCriterios) throws Exception {
		TipoRemessaDocumentFactory tipoRemessaFactory = new TipoRemessaDocumentFactory();
		tipoRemessaFactory.addBarCodeCep(dadosNotificacao);
		dadosNotificacao.setDtEmissao(Util.getDataAtual());
		AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(dadosNotificacao.getCdAit(), TipoStatusEnum.NAI_ENVIADO.getKey());
		dadosNotificacao.setDtMovimento(aitMovimento.getDtMovimento());
		if (dadosNotificacao.getCdEquipamento() > 0) {
		    dadosNotificacao.setNomeEquipamento(EquipamentoEnum.valueOf(dadosNotificacao.getTpEquipamento()));
		} else {
		    dadosNotificacao.setNomeEquipamento(EquipamentoEnum.NENHUM.getValue());
		}
		setarTxtCondutor(dadosNotificacao, reportCriterios);
		if (dadosNotificacao.getDtPrazoDefesa() == null) {
			dadosNotificacao.setDtPrazoDefesa(new PrazoRecursoStrategy()
					.getStrategy(TipoStatusEnum.DEFESA_PREVIA.getKey())
					.gerarPrazo(dadosNotificacao.getCdAit(), customConnection));
		}
		ApresentacaoCondutor apresentacaoCondutor = getCondutorFici(dadosNotificacao.getCdAit());
		if (apresentacaoCondutor != null) {
			dadosNotificacao.setNmCondutor(apresentacaoCondutor.getNmCondutor() != null ? apresentacaoCondutor.getNmCondutor() : null);
			dadosNotificacao.setNrCnhCondutor(apresentacaoCondutor.getNrCnh() != null ? apresentacaoCondutor.getNrCnh() : null);
	        dadosNotificacao.setUfCnhCondutor(apresentacaoCondutor.getUfCnh() != null ? apresentacaoCondutor.getUfCnh() : null);
	        dadosNotificacao.setNrCpfCondutor(apresentacaoCondutor.getNrCpfCnpj() != null ? apresentacaoCondutor.getNrCpfCnpj() : null);
	    }
    	Map<String, Object> assinaturaAutoridade = colaboradorService.buscarAssinaturaAutoridade();
		reportCriterios.addParametros("MOB_IMAGEM_VEICULO", pegarImagemVeiculo(dadosNotificacao.getCdAit()));
		reportCriterios.addParametros("MOB_INFORMACOES_ADICIONAIS_NAI", verificarTipoResponsabilidadeInfracao(dadosNotificacao.getTpResponsabilidade()));
		reportCriterios.addParametros("MOB_TP_DOCUMENTO", TipoStatusEnum.NAI_ENVIADO.getKey());
		reportCriterios.addParametros("LG_GERACAO_VIA_UNICA", true);
		reportCriterios.addParametros("IS_SEGUNDA_VIA", true);
		reportCriterios.addParametros("ASSINATURA_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
				? (byte[]) assinaturaAutoridade.get("assinaturaAutoridade") : null);
		reportCriterios.addParametros("NM_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
				? (String) assinaturaAutoridade.get("nomeAutoridade") : null);
	}
	
	private static void setarTxtCondutor (Notificacao dadosNotificacao, ReportCriterios reportCriterios) throws ValidacaoException{
		int isAr = AitReportValidatorsNAI.verificarAr(reportCriterios.getParametros());
		if (isAr == AitReportServices.MODELO_COM_AR)
			dadosNotificacao.setTxtCondutor("DATA LIMITE P/ APRESENTAÇÃO DO CONDUTOR: " + (dadosNotificacao.getDtPrazoDefesa() != null ? 
				Util.formatDate(dadosNotificacao.getDtPrazoDefesa(), "dd/MM/yyyy") + "." : "EM ATÉ 30 DIAS A CONTAR DO RECEBIMENTO DESTA NOTIFICAÇÃO."));
	}
	
	private byte[] pegarImagemVeiculo(int cdAit) throws Exception {
		this.imgMulta = null;
		this.aitImagem = new AitImagem();
		this.aitImagem = aitImagemService.getFromAit(cdAit);
		if(aitImagem.getBlbImagem() != null ) {
			this.imgMulta = this.aitImagem.getBlbImagem();
		}
		return this.imgMulta;
	}
	
	private String verificarTipoResponsabilidadeInfracao(int tpResponsabilidade) throws Exception {
		String parametro;
		if(tpResponsabilidade == TipoResponsabilidadeInfracaoEnum.MULTA_RESPONSABILIDADE_PROPRIETARIO.getKey()) {
			parametro = parametroService.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_RESPONSABILIDADE_PROPRIETARIO").getTxtValorParametro();
			return parametro;
		}
		parametro = parametroService.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_NAI").getTxtValorParametro();
		return parametro;
	}
	
	private boolean verificarAitNic(int cdAit) throws Exception {
		Ait ait = this.aitRepository.get(cdAit);
		if(ait != null && ait.getCdAitOrigem() > 0) {
			return true;
		}
		return false;
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

}
