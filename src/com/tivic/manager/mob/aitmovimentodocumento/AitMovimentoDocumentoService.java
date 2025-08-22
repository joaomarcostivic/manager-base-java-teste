package com.tivic.manager.mob.aitmovimentodocumento;

import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.List;

import javax.rmi.CORBA.Util;
import javax.ws.rs.BadRequestException;
import javax.xml.bind.ValidationException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoPostagemDOBuilder;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.arquivo.TipoArquivoEnum;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.aitmovimento.validators.ResultadoDefesaValidator;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.builders.AitMovimentoDocumentoDadosProtocoloBuilder;
import com.tivic.manager.ptc.builders.DocumentoBuilder;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepository;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.documentoocorrencia.DocumentoOcorrenciaRepository;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class AitMovimentoDocumentoService implements IAitMovimentoDocumentoService{
	private IArquivoService arquivoService;
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;
	private DocumentoRepository documentoRepository;
	private ApresentacaoCondutorRepository apresentacaoCondutorRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	
	private final int LG_CANCELA_MOVIMENTO = 1;
	
	public AitMovimentoDocumentoService() throws Exception {
		arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class); 
		documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		apresentacaoCondutorRepository = (ApresentacaoCondutorRepository) BeansFactory.get(ApresentacaoCondutorRepository.class);
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}

	public List<AitMovimentoDocumentoDTO> findProcessos(SearchCriterios searchCriterios) throws Exception {
		Search<DocumentoProcesso> searchProcessos = searchProcessos(searchCriterios);
		List<DocumentoProcesso> listDocumentoProcessos = searchProcessos.getList(DocumentoProcesso.class);
		return new AitMovimentoDocumentoDTOBuilder(searchProcessos, listDocumentoProcessos).build();
	}
	
	public List<AitMovimentoDocumentoDTO> findProcessosPublicacao(SearchCriterios searchCriterios) throws Exception {
		Search<DocumentoProcesso> searchProcessos = searchProcessosPublicacao(searchCriterios);
		List<DocumentoProcesso> listDocumentoProcessos = searchProcessos.getList(DocumentoProcesso.class);
		return new AitMovimentoDocumentoDTOBuilder(searchProcessos, listDocumentoProcessos).build();
	}
	
	public List<DocumentoProcesso> getMovimentosFromDTO(SearchCriterios searchCriterios) {
		try {
			searchCriterios.addCriterios("A.dt_publicacao_do", "", ItemComparator.ISNULL, Types.TIME_WITH_TIMEZONE);
			return searchProcessosPublicacao(searchCriterios).getList(DocumentoProcesso.class);
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	public byte[] printRelatorioProcessos(List<DocumentoProcesso> listDocumentoProcessos, GregorianCalendar dtPublicacao, SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			Report report = findDocumentoProcessos(listDocumentoProcessos, dtPublicacao, searchCriterios);
			byte[] reportProcessos = report.getReportDoc("mob/relatorio_processos");
			Arquivo arquivoPublicacao = new ArquivoPostagemDOBuilder().build(reportProcessos, "PROCESSO", TipoArquivoEnum.PROCESSOS_PUBLICADOS.getKey());
			arquivoService.save(arquivoPublicacao, customConnection);
			customConnection.finishConnection();
			return reportProcessos;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Report findDocumentoProcessos(List<DocumentoProcesso> listDocumentoProcessos, GregorianCalendar dtPublicacao, SearchCriterios searchCriterios) throws Exception {
		searchCriterios.getAndRemoveCriterio("A.dt_publicacao_do");
		searchCriterios.addCriterios("A.dt_publicacao_do", DateUtil.formatDate(dtPublicacao, "yyyy-MM-dd HH:mm:ss"), ItemComparator.EQUAL, Types.CHAR);
		ReportCriterios reportCriterios = montarReportCriterios();
		montarDadosDocumento(listDocumentoProcessos, reportCriterios, searchCriterios);
		Report report = new ReportBuilder()
				.list(listDocumentoProcessos)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
	
	private Search<DocumentoProcesso> searchProcessos(SearchCriterios searchCriterios) throws Exception {
		Search<DocumentoProcesso> search = new SearchBuilder<DocumentoProcesso>("mob_ait_movimento A")
				.fields(" A.*, "
					  + " B.ID_AIT, B.NR_PLACA, D.NR_DOCUMENTO,D.NM_REQUERENTE, D.CD_DOCUMENTO, D.DT_PROTOCOLO, D.TXT_OBSERVACAO, B.DT_INFRACAO, E.NM_TIPO_DOCUMENTO,"
					  + "E.ID_TIPO_DOCUMENTO, F.CD_FASE, F.NM_FASE, G.*, H.CD_SITUACAO_DOCUMENTO, H.NM_SITUACAO_DOCUMENTO")
				.addJoinTable(" JOIN mob_ait B ON (A.CD_AIT = B.CD_AIT) ")
				.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.CD_AIT = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
				.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
				.addJoinTable(" LEFT OUTER JOIN ptc_apresentacao_condutor G ON (G.cd_documento = D.cd_documento)" )
				.addJoinTable(" JOIN ptc_situacao_documento H ON (D.cd_situacao_documento = H.cd_situacao_documento)" )
				.searchCriterios(searchCriterios)
				.orderBy(" B.NR_PLACA ")
				.build();
		return search;
	}
	
	private Search<DocumentoProcesso> searchProtocolos(SearchCriterios searchCriterios) throws Exception {
		Search<DocumentoProcesso> search = new SearchBuilder<DocumentoProcesso>("mob_ait_movimento A")
				.fields(" A.CD_AIT, A.CD_MOVIMENTO, A.NR_PROCESSO, A.DT_PUBLICACAO_DO, A.DT_MOVIMENTO, A.CD_OCORRENCIA, A.TP_STATUS, "
					  + " B.ID_AIT, B.NR_PLACA, D.NR_DOCUMENTO,D.NM_REQUERENTE, D.CD_DOCUMENTO, D.DT_PROTOCOLO, D.TXT_OBSERVACAO, D.CD_TIPO_DOCUMENTO, B.DT_INFRACAO, E.NM_TIPO_DOCUMENTO,"
					  + "F.CD_FASE, F.NM_FASE, G.CD_APRESENTACAO_CONDUTOR, G.NM_CONDUTOR, H.CD_SITUACAO_DOCUMENTO, H.NM_SITUACAO_DOCUMENTO")
				.addJoinTable(" JOIN mob_ait B ON (A.CD_AIT = B.CD_AIT) ")
				.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.CD_AIT = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
				.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
				.addJoinTable(" LEFT JOIN ptc_apresentacao_condutor G ON (G.cd_documento = D.cd_documento)" )
				.addJoinTable(" JOIN ptc_situacao_documento H ON (D.cd_situacao_documento = H.cd_situacao_documento)" )
				.searchCriterios(searchCriterios)
				.count()
				.orderBy(" A.CD_AIT ")
				.build();
			
		return search;
	}
	
	private ReportCriterios montarReportCriterios() throws ValidacaoException {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		return reportCriterios;
	}

	private void montarDadosDocumento(List<DocumentoProcesso> listDocumentoProcessos, ReportCriterios reportCriterios, SearchCriterios searchCriterios) throws Exception {
		for (DocumentoProcesso documentoProcesso : listDocumentoProcessos) {
			if (documentoProcesso.getNmTipoDocumento().equals("Apresentação de Condutor")) {
				documentoProcesso.setNmTipoDocumento("Apres. Condutor");
			}
		}
		countProcessos(searchCriterios, reportCriterios);
		countFases(searchCriterios, reportCriterios);
	}
	
	private void countProcessos(SearchCriterios searchCriterios, ReportCriterios reportCriterios) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields(" E.NM_TIPO_DOCUMENTO, COUNT(A.*) AS REGISTROS ")
				.addJoinTable(" JOIN mob_ait B ON (A.CD_AIT = B.CD_AIT) ")
				.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.CD_AIT = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
				.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
				.searchCriterios(searchCriterios)
				.groupBy(" E.CD_TIPO_DOCUMENTO ")
				.build();
		while(search.getRsm().next()) {
			reportCriterios.addParametros(search.getRsm().getString("nm_tipo_documento"), search.getRsm().getInt("registros"));
		}
	}
	
	private void countFases(SearchCriterios searchCriterios, ReportCriterios reportCriterios) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields(" F.NM_FASE, COUNT(A.*) AS REGISTROS ")
				.addJoinTable(" JOIN mob_ait B ON (A.CD_AIT = B.CD_AIT) ")
				.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.CD_AIT = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
				.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
				.searchCriterios(searchCriterios)
				.groupBy(" F.CD_FASE ")
				.build();
		while(search.getRsm().next()) {
			reportCriterios.addParametros(search.getRsm().getString("nm_fase"), search.getRsm().getInt("registros"));
		}
	}

	@Override
	public AitMovimentoDocumentoDTO saveResultado(AitMovimentoDocumentoDTO documento) throws Exception, BadRequestException {
		return saveResultado(documento, new CustomConnection());
	} 

	@Override
	public AitMovimentoDocumentoDTO saveResultado(AitMovimentoDocumentoDTO documentoResultado, CustomConnection customConnection)
			throws Exception, ValidationException, BadRequestException {
		try {
			if(documentoResultado==null)
				throw new ValidacaoException("Erro ao salvar. Documento é nulo.");
			
			customConnection.initConnection(true);
			documentoResultado = new ResultadoDefesaBuilder(documentoResultado).movimento().build();
			new ResultadoDefesaValidator().validate(documentoResultado.getMovimento(), customConnection);
			aitMovimentoRepository.insert(documentoResultado.getMovimento(), customConnection);
			documentoOcorrenciaRepository.insert(documentoResultado.getDocumentoOcorrencia(), customConnection);
			Documento documento = getDadosDocumento(documentoResultado, customConnection);
			documentoRepository.update(documento, customConnection);
			customConnection.finishConnection();
			documentoResultado.setDocumento(documento);
			return documentoResultado;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Documento getDadosDocumento(AitMovimentoDocumentoDTO documentoResultado, CustomConnection customConnection) throws Exception {
		int cdSituacaoJulgado = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_JULGADO", 0, 0, customConnection.getConnection());
		if(cdSituacaoJulgado == 0)
			throw new BadRequestException("O parametro do documento julgado não foi configurado.");
		
		Documento documento = documentoRepository.get(documentoResultado.getDocumento().getCdDocumento(), customConnection);
		documento.setCdFase(documentoResultado.getFase().getCdFase());
		documento.setCdSituacaoDocumento(cdSituacaoJulgado);
		return documento;
	}
	
	public int getAitByDocumento(SearchCriterios searchCriterios) throws Exception {
		Search<AitMovimentoDocumentoDTO> search = new SearchBuilder<AitMovimentoDocumentoDTO>("mob_ait_movimento_documento")
				.searchCriterios(searchCriterios)
				.build();
		if(search.getList(AitMovimentoDocumentoDTO.class).isEmpty())
			throw new ValidacaoException("Nenhum documento encontrado para este Ait");
		
		int cdAit = search.getList(AitMovimentoDocumentoDTO.class).get(0).getAit().getCdAit();
		return cdAit;
	}
	
	@Override
	public PagedResponse<AitMovimentoDocumentoDTO> findProtocolos(SearchCriterios searchCriterios) throws Exception {
		Search<DocumentoProcesso> searchProtocolos = searchProtocolos(searchCriterios);
		List<DocumentoProcesso> listDocumentoProcessos = searchProtocolos.getList(DocumentoProcesso.class);
		
		if(listDocumentoProcessos.isEmpty()) {
			throw new ValidacaoException ("Nenhum protocolo encontrado.");
		} 
		List<AitMovimentoDocumentoDTO> aitDocumento = new AitMovimentoDocumentoDTOBuilder(searchProtocolos, listDocumentoProcessos).build();
		return new AitMovimentoDocumentoPaginatorBuilder(aitDocumento, searchProtocolos.getRsm().getTotal()).build();
	}
	
	@Override
	public DadosProtocoloDTO updateFici(AitMovimentoDocumentoDTO documento)
			throws Exception, ValidationException, BadRequestException {
		return updateFici(documento, new CustomConnection());
	}

	@Override
	public DadosProtocoloDTO updateFici(AitMovimentoDocumentoDTO documento, CustomConnection customConnection)
			throws Exception, ValidationException, BadRequestException {
		try {
			if(documento==null)
				throw new ValidacaoException("Erro ao salvar. Documento é nulo.");
			
			customConnection.initConnection(true);
			Documento documentoUpdate = new DocumentoBuilder(documento.getDocumento()).documento().build();
			documentoUpdate.setCdFase(documento.getFase().getCdFase());
			AitMovimento movimento = aitMovimentoRepository.get(documento.getMovimento().getCdMovimento(), documento.getMovimento().getCdAit());
			movimento.setCdOcorrencia(documento.getMovimento().getCdOcorrencia());
			aitMovimentoRepository.update(movimento, customConnection);
			documentoRepository.update(documentoUpdate, customConnection);
			apresentacaoCondutorRepository.update(documento.getApresentacaoCondutor(), customConnection);
			customConnection.finishConnection();
			return new AitMovimentoDocumentoDadosProtocoloBuilder(documento).documento().apresentacaoCondutor().build();
			
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public AitMovimentoDocumentoDTO cancelaFici(AitMovimentoDocumentoDTO documento)
			throws Exception, ValidationException, BadRequestException {
		return cancelaFici(documento, new CustomConnection());
	}

	@Override
	public AitMovimentoDocumentoDTO cancelaFici(AitMovimentoDocumentoDTO documento, CustomConnection customConnection)
			throws Exception, ValidationException, BadRequestException {
		try {
			if(documento==null)
				throw new ValidacaoException("Erro ao salvar. Documento é nulo.");
			
			customConnection.initConnection(true);
			atualizaCancelamentoMovimento(documento, customConnection);
			cancelaDocumento(documento, customConnection);
			customConnection.finishConnection();
			return documento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void atualizaCancelamentoMovimento(AitMovimentoDocumentoDTO documento, CustomConnection customConnection) throws Exception {
		AitMovimento movimento = aitMovimentoRepository.get(documento.getMovimento().getCdMovimento(), documento.getMovimento().getCdAit());
		movimento = cancelaMovimento(movimento, documento, customConnection);
		aitMovimentoRepository.update(movimento, customConnection);
	}
	
	private AitMovimento cancelaMovimento(AitMovimento movimento, AitMovimentoDocumentoDTO documento, 
			CustomConnection customConnection) throws Exception {
		if(movimento.getLgEnviadoDetran() == AitMovimentoServices.REGISTRADO) {	
			AitMovimento cancelaFiciMovimento = new AitMovimentoBuilder()
												.setCdMovimento(documento.getMovimento().getCdMovimento())
												.setCdAit(documento.getAit().getCdAit())
												.setDtMovimento(new GregorianCalendar())
												.setTpStatus(Integer.parseInt(documento.getTipoDocumento().getIdTipoDocumento()))
												.setDsObservacao(documento.getDocumento().getTxtDocumento())
												.setDtDigitacao(new GregorianCalendar())
												.setCdUsuario(documento.getUsuario().getCdUsuario())
												.setTpArquivo(documento.getFase().getCdFase())
												.setNrProcesso(generateNrProcesso(documento))
												.setCdOcorrencia(documento.getMovimento().getCdOcorrencia())
												.build();
			cancelaFiciMovimento.setTpStatus(TipoStatusEnum.CANCELAMENTO_FICI.getKey());
			aitMovimentoRepository.insert(cancelaFiciMovimento, customConnection);
		} else {
			movimento.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIAR);
		}
		
		return movimento;
	}
	
	private void cancelaDocumento(AitMovimentoDocumentoDTO documento, CustomConnection customConnection) throws Exception {
		Documento novoDocumento = documentoRepository.get(documento.getDocumento().getCdDocumento());
		novoDocumento.setCdSituacaoDocumento(getCdSituacaoCancelado());
		documentoRepository.update(novoDocumento, customConnection);
		
	}
	
	private int getCdSituacaoCancelado() {
		return ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_CANCELADO", 0, 0, null);
	}
	
	private Search<DocumentoProcesso> searchProcessosPublicacao(SearchCriterios searchCriterios) throws Exception {
		Search<DocumentoProcesso> search = new SearchBuilder<DocumentoProcesso>("mob_ait_movimento A")
				.fields(" A.*, "
					  + " B.ID_AIT, B.NR_PLACA, D.NR_DOCUMENTO,D.NM_REQUERENTE, D.CD_DOCUMENTO, D.DT_PROTOCOLO, D.TXT_OBSERVACAO, B.DT_INFRACAO, E.NM_TIPO_DOCUMENTO,"
					  + "F.CD_FASE, F.NM_FASE")
				.addJoinTable(" JOIN mob_ait B ON (A.CD_AIT = B.CD_AIT) ")
				.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.CD_AIT = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
				.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
				.searchCriterios(searchCriterios)
				.orderBy(" B.NR_PLACA ")
				.build();
		return search;
	}
	
	@Override
	public AitMovimentoDocumento getMovimentoDocumento(SearchCriterios searchCriterios) throws Exception {
		return getMovimentoDocumento(searchCriterios, new CustomConnection());
	}
	
	@Override
	public AitMovimentoDocumento getMovimentoDocumento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			Search<AitMovimentoDocumento> search = new SearchBuilder<AitMovimentoDocumento>("mob_ait_movimento_documento")
					.fields("*")
					.searchCriterios(searchCriterios)
					.customConnection(customConnection)
				.build();
			List<AitMovimentoDocumento> aitMovimentoDocumento = search.getList(AitMovimentoDocumento.class);
			
			if(aitMovimentoDocumento.isEmpty()) {
				throw new Exception("Nenhum Movimento Documento encontrado.");
			}
			
			customConnection.finishConnection();
			return aitMovimentoDocumento.get(0);
			} finally {
				customConnection.closeConnection();
			}
	}
	
	private String generateNrProcesso(AitMovimentoDocumentoDTO dadosProtocolo) {
		if (dadosProtocolo.getMovimento().getNrProcesso() != null) {
			return dadosProtocolo.getMovimento().getNrProcesso();
		} else {
			if (dadosProtocolo.getDocumento().getNrDocumento().length() <= 16)
				return dadosProtocolo.getDocumento().getNrDocumentoExterno();
			else
				return dadosProtocolo.getDocumento().getNrDocumento();
		}
	}
}
