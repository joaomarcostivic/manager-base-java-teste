package com.tivic.manager.ptc.protocolosv3.print;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.TipoDocumento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.fase.IFaseRepository;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolosv3.documento.ata.AtaDTO;
import com.tivic.manager.ptc.protocolosv3.documento.situacao.ISituacaoDocumentoRepository;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.tipodocumento.TipoDocumentoRepository;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ProtocoloPrintService implements IProtocoloPrintService {

	private DocumentoRepository documentoRepository;
	private TipoDocumentoRepository tipoDocumentoRepository;
	private AitRepository aitRepository;
	private IFaseRepository faseRepository;
	private ISituacaoDocumentoRepository situacaoDocumentoRepository;
	private InfracaoRepository infracaoRepository;
	private int relatorioDefault = 1;
	private IAitMovimentoService aitMovimentoService;
	private IParametroRepository parametroRepository;
	private IParametroService parametroService;

	
	public ProtocoloPrintService() throws Exception {
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.tipoDocumentoRepository = (TipoDocumentoRepository) BeansFactory.get(TipoDocumentoRepository.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.faseRepository = (IFaseRepository) BeansFactory.get(IFaseRepository.class);
		this.situacaoDocumentoRepository = (ISituacaoDocumentoRepository) BeansFactory.get(ISituacaoDocumentoRepository.class);
		this.infracaoRepository = (InfracaoRepository) BeansFactory.get(InfracaoRepository.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
	}
	
	public byte[] imprimirProtocolo(int cdAit, int cdDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Documento documento = this.documentoRepository.get(cdDocumento, customConnection);
			Ait ait = this.aitRepository.get(cdAit, customConnection);
			String reportName = getNmRelatorio(documento);
			Report report = new ReportBuilder()
					.search(gerarSearchRelatorio(cdDocumento))
					.reportCriterios(montarReportCriterios(documento, ait))
					.build();
			customConnection.finishConnection();
			return report.getReportPdf(reportName);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private String getNmRelatorio(Documento documento) throws Exception {
		int tpImpressaoProtocolo = ParametroServices.getValorOfParametroAsInteger("MOB_LG_IMPRIMIR_PROTOCOLO_GERAL", 0);
		if(tpImpressaoProtocolo != this.relatorioDefault) {
			return "mob/protocolo";
		} else {
			int cdFasePendente = ParametroServices.getValorOfParametroAsInteger("CD_FASE_PENDENTE", 0);
			return documento.getCdFase() == cdFasePendente ? "mob/protocolo_2_vias" : "mob/protocolo_julgamento";
		}
	}
	
	private Search<AtaDTO> gerarSearchRelatorio(int cdDocumento) throws Exception {
		Search<AtaDTO> search = new SearchBuilder<AtaDTO>("PTC_DOCUMENTO A")
				.fields("txt_Observacao AS ds_observacao, *")
				.searchCriterios(montarCriterios(cdDocumento))
				.build();
		return search;
	}
	
	private SearchCriterios montarCriterios(int cdDocumento) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_documento", cdDocumento, true);
		return searchCriterios;
	}
	
	private ReportCriterios montarReportCriterios(Documento documento, Ait ait) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE"));
		reportCriterios.addParametros("NR_TELEFONE_2", ParametroServices.getValorOfParametro("NR_TELEFONE2"));
		reportCriterios.addParametros("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("NM_REQUERENTE", documento.getNmRequerente() != null ? documento.getNmRequerente() : "Não Informado.");	
		reportCriterios.addParametros("NM_TITULO", getNmTipoDocumento(documento.getCdTipoDocumento()));
		reportCriterios.addParametros("DS_DT_PROTOCOLO", Util.formatDate(documento.getDtProtocolo(), "dd/MM/yyyy"));
		reportCriterios.addParametros("DS_DT_JULGAMENTO", pegarDataJulgamento(documento));
		reportCriterios.addParametros("NR_AIT", ait.getIdAit());
		reportCriterios.addParametros("NR_DOCUMENTO", documento.getNrDocumento());
		reportCriterios.addParametros("NR_PLACA", ait.getNrPlaca());
		reportCriterios.addParametros("NM_FASE", getNmFase(documento.getCdFase()));
		reportCriterios.addParametros("NM_SITUACAO", getNmSituacaoDocumento(documento.getCdSituacaoDocumento()));
		reportCriterios.addParametros("NR_CONTROLE", ait.getNrControle());
		reportCriterios.addParametros("NR_INFRACAO", getInfracao(ait.getCdInfracao()).getNrCodDetran());
		reportCriterios.addParametros("DS_INFRACAO", getInfracao(ait.getCdInfracao()).getDsInfracao());
		reportCriterios.addParametros("DS_DT_INFRACAO", Util.formatDate(ait.getDtInfracao(), "dd/MM/yyyy"));
		reportCriterios.addParametros("DS_DT_LIMITE", getDtPorTipoJulgamento(documento, ait));
		reportCriterios.addParametros("LABEL_LIMITE", getLabelDtLimite(documento));
		reportCriterios.addParametros("NM_CONDUTOR_APRESENTADO", getNmCondutor(documento, ait));
		reportCriterios.addParametros("NM_TEMPESTIVIDADE", getTempestividade(documento, ait));
		reportCriterios.addParametros("TXT_PARECER_JULGAMENTO", getParecerJulgamento(documento));
		return reportCriterios;
	}
	
	private String pegarDataJulgamento(Documento documento) throws Exception {
		int codigoFasePendente = ParametroServices.getValorOfParametroAsInteger("CD_FASE_PENDENTE", 0);
		if (documento.getCdFase() == codigoFasePendente)
			return Util.formatDate(documento.getDtProtocolo(), "dd/MM/yyyy");
		if (documento.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.APRESENTACAO_CONDUTOR.getKey()) {
			return Util.formatDate(documento.getDtProtocolo(), "dd/MM/yyyy");
		} else {
			DocumentoOcorrencia documentoOcorrencia = buscarOcorrenciaJulgamento(documento);
			if(documentoOcorrencia != null) {
				return Util.formatDate(documentoOcorrencia.getDtOcorrencia(), "dd/MM/yyyy"); 				
			}
		}
		return null;
	}
	
	private String getParecerJulgamento(Documento documento) throws Exception {
		int codigoFasePendente = this.parametroRepository.getValorOfParametroAsInt("CD_FASE_PENDENTE");
		if (documento.getCdFase() != codigoFasePendente) {
			DocumentoOcorrencia documentoOcorrencia = buscarOcorrenciaJulgamento(documento);
			if(documentoOcorrencia != null && documentoOcorrencia.getTxtOcorrencia() != null) {
				return documentoOcorrencia.getTxtOcorrencia();
			}
		}
		return null;
	}
	
	private DocumentoOcorrencia buscarOcorrenciaJulgamento(Documento documento) throws Exception {
		int codigoSituacaoDeferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0);
		int codigoOcorrenciaDeferido = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_DEFERIDA", 0);
		int codigoOcorrenciaIndeferida = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_INDEFERIDA", 0);
		int cdOcorrencia = documento.getCdSituacaoDocumento() == codigoSituacaoDeferido ? codigoOcorrenciaDeferido : codigoOcorrenciaIndeferida;
		SearchCriterios searchCriterios = criteriosMovimento(documento.getCdDocumento(), cdOcorrencia);
		List<DocumentoOcorrencia> documentoOcorrencia = findOcorrenciaJulgamento(searchCriterios);
		return !documentoOcorrencia.isEmpty() ? documentoOcorrencia.get(0): null;
	}
	
	public List<DocumentoOcorrencia> findOcorrenciaJulgamento(SearchCriterios searchCriterios) throws Exception {
		Search<DocumentoOcorrencia> search = new SearchBuilder<DocumentoOcorrencia>("ptc_documento_ocorrencia")
				.searchCriterios(searchCriterios)
				.orderBy("cd_ocorrencia desc")
				.build();
		List<DocumentoOcorrencia> documentoOcorrenciaList = search.getList(DocumentoOcorrencia.class);
		return documentoOcorrenciaList;
	}
	
	private SearchCriterios criteriosMovimento(int cdDocumento, int cdTipoOcorrencia) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_documento", cdDocumento, true);
		searchCriterios.addCriteriosEqualInteger("cd_tipo_ocorrencia", cdTipoOcorrencia, true);
		searchCriterios.setQtLimite(1);
		return searchCriterios;
	}
	
	private String getNmTipoDocumento(int cdTipoDocumento) throws Exception {
		TipoDocumento tipoDocumento = this.tipoDocumentoRepository.get(cdTipoDocumento);
		return tipoDocumento.getNmTipoDocumento();
	}
	
	private String getNmFase(int cdFase) throws Exception {
		Fase fase = this.faseRepository.get(cdFase);
		return fase.getNmFase();
	}
	
	private String getNmSituacaoDocumento(int cdSituacaoDocumento) throws Exception {
		SituacaoDocumento situacaoDocumento = this.situacaoDocumentoRepository.get(cdSituacaoDocumento);
		return situacaoDocumento.getNmSituacaoDocumento();
	}
	
	private Infracao getInfracao(int cdInfracao) throws Exception {
		Infracao infracao = this.infracaoRepository.get(cdInfracao);
		return infracao;
	}
	
	private String getNmCondutor(Documento documento, Ait ait) throws Exception {
		AitMovimento contemMovimentoApresentacaoCondutor = aitMovimentoService.getMovimentoTpStatus(ait.getCdAit(), TipoStatusEnum.TRANSFERENCIA_PONTUACAO.getKey());
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("B.id_ait", ait.getIdAit());
		int tipoDocumentoApresentacaoCondutor = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", 0);
		if(documento.getCdTipoDocumento() == tipoDocumentoApresentacaoCondutor || contemMovimentoApresentacaoCondutor.getCdMovimento() != 0) {
			String nmCondutor = buscarNmCondutor(searchCriterios, new CustomConnection()) != null ? buscarNmCondutor(searchCriterios, new CustomConnection()): null;
			return nmCondutor;		
		}
		return null;
	}
	
	private String buscarNmCondutor(SearchCriterios criterios, CustomConnection customConnection) throws Exception {
		Search<ApresentacaoCondutor> search = new SearchBuilder<ApresentacaoCondutor>("MOB_AIT_MOVIMENTO A")
				.fields("G.NM_CONDUTOR")
				.addJoinTable("JOIN MOB_AIT B ON (A.CD_AIT = B.CD_AIT)")
				.addJoinTable("JOIN MOB_AIT_MOVIMENTO_DOCUMENTO C ON (C.CD_AIT = B.CD_AIT AND A.CD_MOVIMENTO = C.CD_MOVIMENTO)")
				.addJoinTable("JOIN PTC_DOCUMENTO D ON (C.CD_DOCUMENTO = D.CD_DOCUMENTO)")
				.addJoinTable("JOIN PTC_APRESENTACAO_CONDUTOR G ON (G.CD_DOCUMENTO = D.CD_DOCUMENTO)")
				.searchCriterios(criterios)
				.additionalCriterias(" D.cd_fase <> " + parametroService.getValorOfParametroAsInt("CD_SITUACAO_DOCUMENTO_CANCELADO").getNrValorParametro())
				.build();
		return search.getList(ApresentacaoCondutor.class).isEmpty() ? null : search.getList(ApresentacaoCondutor.class).get(0).getNmCondutor();
	}
	
	private String getDtPorTipoJulgamento(Documento documento, Ait ait) {
	    return documento.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey() ?
	           Util.formatDate(ait.getDtVencimento(), "dd/MM/yyyy") : Util.formatDate(ait.getDtPrazoDefesa(), "dd/MM/yyyy");
	}
	
	private String getLabelDtLimite(Documento documento) {
		return documento.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey() ? "DATA LIMITE DO RECURSO" : "DATA LIMITE DA DEFESA";
	}
	
	private String getTempestividade(Documento documento, Ait ait) throws Exception {
	    if (isIntempestivo(documento, ait)) {
	        return "Intempestivo";
	    }
	    if (isTempestivo(documento, ait)) {
	        return "Tempestivo";
	    }
	    return null;
	} 

	private boolean isIntempestivo(Documento documento, Ait ait) {
		if (documento.getDtProtocolo() != null) {
			if (verificarTipoDocumentoProtocolo(documento.getCdTipoDocumento()) && ait.getDtPrazoDefesa() != null) {
				return isDocumentoProtocoloIntempestivo(documento, ait);
			}
			if (documento.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey() && ait.getDtVencimento() != null) {
				return isRecursoJariIntempestivo(documento, ait);
			}
		}
		return false;
	}

	private boolean isTempestivo(Documento documento, Ait ait) {
		if (documento.getDtProtocolo() != null) {
			if (verificarTipoDocumentoProtocolo(documento.getCdTipoDocumento()) && ait.getDtPrazoDefesa() != null) {
				return isDocumentoProtocoloTempestivo(documento, ait);
			}
			if (documento.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey()	&& ait.getDtVencimento() != null) {
				return isRecursoJariTempestivo(documento, ait);
			}
		}
		return false;
	}

	private boolean isDocumentoProtocoloIntempestivo(Documento documento, Ait ait) {
		zerarTempo(documento.getDtProtocolo());
		zerarTempo(ait.getDtPrazoDefesa());
		return documento.getDtProtocolo().after(ait.getDtPrazoDefesa());
	}
	
	private boolean isDocumentoProtocoloTempestivo(Documento documento, Ait ait) {
		zerarTempo(documento.getDtProtocolo());
		zerarTempo(ait.getDtPrazoDefesa());
		return documento.getDtProtocolo().equals(ait.getDtPrazoDefesa())
				|| documento.getDtProtocolo().before(ait.getDtPrazoDefesa());
	}
	
	private boolean isRecursoJariIntempestivo(Documento documento, Ait ait) {
		zerarTempo(documento.getDtProtocolo());
		zerarTempo(ait.getDtVencimento());
		return documento.getDtProtocolo().after(ait.getDtVencimento());
	}
	
	private boolean isRecursoJariTempestivo(Documento documento, Ait ait) {
		zerarTempo(documento.getDtProtocolo());
		zerarTempo(ait.getDtVencimento());
		return documento.getDtProtocolo().equals(ait.getDtVencimento())
				|| documento.getDtProtocolo().before(ait.getDtVencimento());
	}

	private void zerarTempo(GregorianCalendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	private Boolean verificarTipoDocumentoProtocolo(int cdTipoDocumento) {
		List<Integer> tiposDocumento = new ArrayList<Integer>();
		tiposDocumento.add(TipoDocumentoProtocoloEnum.APRESENTACAO_CONDUTOR.getKey());
		tiposDocumento.add(TipoDocumentoProtocoloEnum.DEFESA_PREVIA.getKey());
		tiposDocumento.add(TipoDocumentoProtocoloEnum.DEFESA_PREVIA_ADVERTENCIA.getKey());
		return tiposDocumento.contains(cdTipoDocumento);
	}
}
