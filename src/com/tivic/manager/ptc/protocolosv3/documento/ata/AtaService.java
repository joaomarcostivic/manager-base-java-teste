package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.groovy.syntax.Types;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.grl.pessoavinculohistorico.enums.StPessoaVinculoEnum;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolos.documentoocorrencia.DocumentoOcorrenciaRepository;
import com.tivic.manager.ptc.protocolosv3.documento.ocorrencia.DocumentoOcorrenciaBuilder;
import com.tivic.manager.ptc.protocolosv3.documento.ocorrencia.SituacaoDocumentoOcorrenciaFactory;
import com.tivic.manager.ptc.protocolosv3.documento.situacao.IUpdateSituacaoDocumento;
import com.tivic.manager.ptc.protocolosv3.documento.situacao.SituacaoDocumentoFactory;
import com.tivic.manager.ptc.protocolosv3.recursos.IRecursoRepository;
import com.tivic.manager.ptc.protocolosv3.recursos.RecursoBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class AtaService implements IAtaService {
	private IAtaRepository ataRepository;
	private IAitMovimentoService aitMovimentoService;
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;
	private IAtaRelatorService ataRelatorService;
	private IRecursoRepository recursoRepository;
	private IAitService aitService;
	private DocumentoRepository documentoRepository;
	private GenerateReportsAta generateReportsAta;
	private ManagerLog managerLog;
	private IParametroRepository parametroRepository;
	
	public AtaService() throws Exception {
		ataRepository = (IAtaRepository) BeansFactory.get(IAtaRepository.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
		ataRelatorService = (IAtaRelatorService) BeansFactory.get(IAtaRelatorService.class);
		recursoRepository = (IRecursoRepository) BeansFactory.get(IRecursoRepository.class);
		aitService = (IAitService) BeansFactory.get(IAitService.class);
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		generateReportsAta = new GenerateReportsAta();
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	@Override
	public AtaDTO getAtaDTOJari(SearchCriterios searchCriterios) throws Exception {
	    Search<AtaDTO> search = getAtaJari(searchCriterios);

	    if (search.getList(AtaDTO.class).isEmpty()) {
	        throw new ValidacaoException("Não há Jari registrada com este número de Ait.");
	    }
	    AtaDTO ataDto = search.getList(AtaDTO.class).get(0);
	    if (ataDto.getCdFase() == getParametroValue("CD_FASE_JULGADO")) {
	        throw new ValidacaoException("Este AIT já está presente em outra ATA.");
	    } else if (ataDto.getCdFase() == getParametroValue("CD_FASE_CANCELADO")) {
	        throw new ValidacaoException("Este AIT está com julgamento cancelado.");
	    } else if (ataDto.getCdFase() == getParametroValue("CD_FASE_PENDENTE")) {
	        return ataDto; 
	    }
	    throw new ValidacaoException("Fase desconhecida para este AIT.");
	}
	
	private Search<AtaDTO> getAtaJari(SearchCriterios searchCriterios) throws Exception {
		Search<AtaDTO> search = new SearchBuilder<AtaDTO>("mob_ait A")
				.fields("A.id_ait, A.cd_ait, B.cd_movimento, A.tp_status, B.dt_movimento AS dt_entrada_recurso," +
						" D.nr_documento AS nr_protocolo, D.cd_documento, D.nm_requerente, G.nm_tipo_documento, F.cd_fase ")
				.addJoinTable("INNER JOIN mob_ait_movimento B ON(A.cd_ait = B.cd_ait)")
				.addJoinTable("INNER JOIN mob_ait_movimento_documento C ON(C.cd_ait = B.cd_ait)")
				.addJoinTable("INNER JOIN ptc_documento D ON(C.cd_documento = D.cd_documento)")
				.addJoinTable("INNER JOIN ptc_situacao_documento E ON(E.cd_situacao_documento = D.cd_situacao_documento)")
				.addJoinTable("INNER JOIN ptc_fase F ON(F.cd_fase = D.cd_fase)")
				.addJoinTable("INNER JOIN gpn_tipo_documento G ON(G.cd_tipo_documento = D.cd_tipo_documento)")
				.searchCriterios(searchCriterios)
				.additionalCriterias("B.lg_enviado_detran = " + TipoStatusEnum.ENVIADO_AO_DETRAN.getKey() 
					+ " AND B.tp_status = " + TipoStatusEnum.RECURSO_JARI.getKey() 
				).build();
		return search;
	}
	
	private int getParametroValue(String nmParametro) throws Exception {
		int vlParametro = parametroRepository.getValorOfParametroAsInt(nmParametro);
		if(vlParametro == 0)
			throw new IllegalArgumentException("O parâmetro " + nmParametro + " não foi configurado.");
		return vlParametro;
	}
	
	@Override
	public Ata getAtaById(String idAta) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_ata", idAta);
		List<Ata> ataList = ataRepository.find(searchCriterios, new CustomConnection());
		if(ataList.isEmpty()) return null;
		return ataList.get(0);
	}
	
	@Override
	public AtaDTO insert(AtaDTO ataDto) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			validateIdAta(ataDto.getIdAta(), customConnection);
			Ata ata = new AtaBuilder()
					.addCdUsuario(ataDto.getCdUsuario())
					.addDtAta(ataDto.getDtAta())
					.addIdAta(ataDto.getIdAta())
					.build();
			ataRepository.insert(ata, customConnection);
			ataDto.setCdAta(ata.getCdAta());
			dataJulgamentoValidate(ataDto, customConnection);
			AitMovimento aitMovimento = gerarAitMovimento(ataDto, customConnection);
			aitMovimentoService.insert(aitMovimento, customConnection);
			atualizarAit(aitMovimento, customConnection);
			gerarDocumentoOcorrencia(ataDto, customConnection);
			recursoRepository.insert(new RecursoBuilder()
									.addCdDocumento(ataDto.getCdDocumento())
									.addCdAta(ata.getCdAta())
									.build(), customConnection);
			salvarRelatores(ata.getCdAta(), customConnection);
			SituacaoDocumentoFactory factory = new SituacaoDocumentoFactory();
			IUpdateSituacaoDocumento strategy = factory.getStrategy(ataDto.getTpStatus());
			strategy.setSituacaoDocumento(ataDto.getCdDocumento(), ataDto.getDsAssunto(), customConnection);
			customConnection.finishConnection();
			return ataDto;	
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<AtaDTO> insertList(List<AtaDTO> listAta) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			validateIdAta(listAta.get(0).getIdAta(), customConnection);
			Ata ata = new AtaBuilder()
					.addCdUsuario(listAta.get(0).getCdUsuario())
					.addDtAta(listAta.get(0).getDtAta())
					.addIdAta(listAta.get(0).getIdAta())
					.build();
			ataRepository.insert(ata, customConnection);
			for (AtaDTO ataDto : listAta) {
				AitMovimento aitMovimento = gerarAitMovimento(ataDto, customConnection);
				ataDto.setCdAta(ata.getCdAta());
				dataJulgamentoValidate(ataDto, customConnection);
				aitMovimentoService.insert(aitMovimento, customConnection);
				atualizarAit(aitMovimento, customConnection);
				gerarDocumentoOcorrencia(ataDto, customConnection);
				recursoRepository.insert(new RecursoBuilder()
										.addCdDocumento(ataDto.getCdDocumento())
										.addCdAta(ata.getCdAta())
										.build(), customConnection);
				SituacaoDocumentoFactory factory = new SituacaoDocumentoFactory();
				IUpdateSituacaoDocumento strategy = factory.getStrategy(ataDto.getTpStatus());
				strategy.setSituacaoDocumento(ataDto.getCdDocumento(), ataDto.getDsObservacao(), customConnection);
			}
			salvarRelatores(ata.getCdAta(), customConnection);
			customConnection.finishConnection();
			return listAta;	
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public AtaDTO updateListAta(AtaDTO ataDto) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			getAtaByID(ataDto, customConnection);
			AitMovimento aitMovimento = gerarAitMovimento(ataDto, customConnection);
			aitMovimentoService.insert(aitMovimento, customConnection);
			atualizarAit(aitMovimento, customConnection);
			gerarDocumentoOcorrencia(ataDto, customConnection);
			recursoRepository.insert(new RecursoBuilder()
									.addCdDocumento(ataDto.getCdDocumento())
									.addCdAta(ataDto.getCdAta())
									.build(), customConnection);
			new SituacaoDocumentoFactory().getStrategy(ataDto.getTpStatus()).setSituacaoDocumento(ataDto.getCdDocumento(), ataDto.getDsAssunto(), customConnection);
			customConnection.finishConnection();
			return ataDto;	
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<AtaDTO> updateAtaJari(List<AtaDTO> listAta) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			for (AtaDTO ataDto : listAta) {
				getAtaByID(ataDto, customConnection);
				AitMovimento aitMovimento = gerarAitMovimento(ataDto, customConnection);
				aitMovimentoService.insert(aitMovimento, customConnection);
				atualizarAit(aitMovimento, customConnection);
				gerarDocumentoOcorrencia(ataDto, customConnection);
				recursoRepository.insert(new RecursoBuilder()
										.addCdDocumento(ataDto.getCdDocumento())
										.addCdAta(ataDto.getCdAta())
										.build(), customConnection);
				new SituacaoDocumentoFactory().getStrategy(ataDto.getTpStatus()).setSituacaoDocumento(ataDto.getCdDocumento(), ataDto.getDsAssunto(), customConnection);
				customConnection.finishConnection();
			}
			customConnection.finishConnection();
			return listAta;	
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void getAtaByID(AtaDTO ataDto, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_ata", ataDto.getIdAta());
		List<Ata> ataList = ataRepository.find(searchCriterios, customConnection);
		new AtaDTOBuilder(ataDto).montaByAta(ataList.get(0));
	}
	
	private void atualizarAit(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		Ait ait = aitService.get(aitMovimento.getCdAit(), customConnection);
		ait.setCdMovimentoAtual(aitMovimento.getCdMovimento());
		ait.setTpStatus(aitMovimento.getTpStatus());
		aitService.update(ait, customConnection);
	}
	
	private List<Integer> buscarRelatoresAtivos(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("B.nm_vinculo", "AUTORIDADE DE TRÂNSITO", ItemComparator.DIFFERENT, java.sql.Types.VARCHAR);
		searchCriterios.addCriterios("B.nm_vinculo", "AGENTE", ItemComparator.DIFFERENT, java.sql.Types.VARCHAR);
		searchCriterios.addCriteriosEqualInteger("A.st_vinculo", StPessoaVinculoEnum.ST_ATIVO.getKey());
		Search<PessoaEmpresa> relatores = new SearchBuilder<PessoaEmpresa>("grl_pessoa_empresa A")
				.fields("A.cd_pessoa")
				.addJoinTable("JOIN grl_vinculo B ON (A.cd_vinculo = B.cd_vinculo)")
				.searchCriterios(searchCriterios).customConnection(customConnection)
				.build();
		return relatores.getList(PessoaEmpresa.class).stream()
	            .map(PessoaEmpresa::getCdPessoa)
	            .collect(Collectors.toList());
	}
	
	public void salvarRelatores(int cdAta, CustomConnection customConnection) throws IllegalArgumentException, Exception {
		List<Integer> cdRelatores = buscarRelatoresAtivos(customConnection);
		if (cdRelatores.size() != 0)
			ataRelatorService.insertRelatores(cdAta, cdRelatores, customConnection);
	}
	
	public Search<Ata> find(SearchCriterios searchCriterios) throws Exception {
		Search<Ata> search = new SearchBuilder<Ata>("ptc_ata")
				.searchCriterios(searchCriterios)
				.count()
				.build();
		return search;
	}
	
	public Search<AtaDTO> findAtaDTO(SearchCriterios searchCriterios) throws Exception {
		Search<AtaDTO> search = new SearchBuilder<AtaDTO>("ptc_ata A")
				.fields(" DISTINCT ON (A.cd_ata, A.dt_ata) A.*, A.dt_cadastro AS dt_entrada_recurso, E.id_ait, F.nr_documento as nr_protocolo, H.nm_pessoa, I.nm_situacao_documento ")
				.addJoinTable("INNER JOIN PTC_RECURSO B ON (A.CD_ATA = B.CD_ATA)")
				.addJoinTable("INNER JOIN MOB_AIT_MOVIMENTO_DOCUMENTO C ON (B.CD_DOCUMENTO = C.CD_DOCUMENTO)")
				.addJoinTable("INNER JOIN MOB_AIT_MOVIMENTO D ON (C.CD_MOVIMENTO = D.CD_MOVIMENTO AND C.CD_AIT = D.CD_AIT)")
				.addJoinTable("INNER JOIN MOB_AIT E ON (D.CD_AIT = E.CD_AIT)")
				.addJoinTable("INNER JOIN PTC_DOCUMENTO F ON(F.CD_DOCUMENTO = B.CD_DOCUMENTO)")
				.addJoinTable("LEFT OUTER JOIN seg_usuario G ON(A.cd_usuario = G.cd_usuario)")
				.addJoinTable("LEFT OUTER JOIN grl_pessoa H ON(H.cd_pessoa = G.cd_pessoa)")
				.addJoinTable("LEFT OUTER JOIN ptc_situacao_documento I ON (F.cd_situacao_documento = I.cd_situacao_documento)")
				.searchCriterios(searchCriterios)
				.orderBy("A.dt_ata DESC")
				.count()
				.build();
		if(search.getList(AtaDTO.class).isEmpty()) 
			throw new ValidacaoException ("Nenhum registro encontrado!");
		return search;
	}
	
	public Search<AtaDTO> findOcorrenciaDTO(SearchCriterios searchCriterios) throws Exception {
		Search<AtaDTO> search = new SearchBuilder<AtaDTO>("ptc_documento_ocorrencia A")
				.fields( " A.dt_ocorrencia AS dt_entrada_recurso,  A.txt_ocorrencia as ds_ocorrencia, "
					   + " C.id_ata, D.nm_tipo_ocorrencia AS nm_situacao_documento, F.nm_pessoa ")
				.addJoinTable("LEFT JOIN ptc_recurso B on (B.cd_documento = A.cd_documento)")
				.addJoinTable("LEFT JOIN ptc_ata C on (C.cd_ata = B.cd_ata)")
				.addJoinTable("LEFT JOIN grl_tipo_ocorrencia D on (D.cd_tipo_ocorrencia = A.cd_tipo_ocorrencia)")
				.addJoinTable("LEFT OUTER JOIN seg_usuario E ON(A.cd_usuario = E.cd_usuario)")
				.addJoinTable("LEFT OUTER JOIN grl_pessoa F ON(F.cd_pessoa = E.cd_pessoa)")
				.searchCriterios(searchCriterios)
				.orderBy("A.dt_ocorrencia DESC")
				.count()
				.build();
		if(search.getList(AtaDTO.class).isEmpty()) 
			throw new ValidacaoException ("Nenhum registro encontrado!");
		return search;
	}
	
	private void validateIdAta(String idAta, CustomConnection customConnection) throws Exception {
		List<Ata> ataList = new ArrayList<Ata>();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_ata", idAta);
		ataList = ataRepository.find(searchCriterios, customConnection);
		if(!ataList.isEmpty()) 
			throw new Exception("Já existe uma ATA registrada com esse número de ATA!");
	}
	
	private void gerarDocumentoOcorrencia(AtaDTO ataDto, CustomConnection customConnection) throws Exception {
		DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrenciaBuilder()
				.addCdDocumento(ataDto.getCdDocumento())
				.addDtOcorrencia(ataDto.getDtAta())
				.addCdUsuario(ataDto.getCdUsuario())
				.addTxtOcorrencia(ataDto.getTxtOcorrencia())
				.addTpConsistencia(ataDto.getTpConsistencia())
				.build();
		documentoOcorrencia = new SituacaoDocumentoOcorrenciaFactory().getStrategy(ataDto.getTpStatus()).montarDocumentoOcorrencia(documentoOcorrencia);
		documentoOcorrenciaRepository.insert(documentoOcorrencia, customConnection); 
	}
	
	private AitMovimento gerarAitMovimento(AtaDTO ataDto, CustomConnection customConnection) throws Exception {
		AitMovimento movimentoEntrada = searchEntradaJari(ataDto.getCdAit());
		if(movimentoEntrada.getLgEnviadoDetran() != TipoStatusEnum.ENVIADO_AO_DETRAN.getKey()) 
			throw new Exception("É necessário registrar no Detran a entrada do protocolo para salvar o resultado do julgamento.");
		AitMovimento movimentoJulgamento = new AitMovimentoBuilder()
				.setCdAit(ataDto.getCdAit())
				.setCdUsuario(ataDto.getCdUsuario())
				.setDtMovimento(ataDto.getDtAta())
				.setDtDigitacao(new GregorianCalendar())
				.setNrProcesso(movimentoEntrada.getNrProcesso())
				.setTpStatus(ataDto.getTpStatus())
				.build();
		return movimentoJulgamento;
	}
	
	private AitMovimento searchEntradaJari(int cdAit) throws Exception {
		AitMovimento aitMovimento = new AitMovimento();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.RECURSO_JARI.getKey());
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>(" mob_ait_movimento ")
				.searchCriterios(searchCriterios)
				.orderBy("cd_movimento DESC")
				.build();
		List<AitMovimento> listAitMovimento = search.getList(AitMovimento.class);
	    aitMovimento = listAitMovimento.get(0);
	    if(listAitMovimento.isEmpty()) 
			throw new Exception("Não há entrada de Recurso JARI para esse AIT.");
	    return aitMovimento;
	}
	
	public byte[] getBoletimReport(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			Report report = new ReportBuilder()
					.search(generateReportsAta.gerarSearchRelatorio(searchCriterios))
					.reportCriterios(generateReportsAta.montarReportCriterios())
					.build();
			byte[] print = report.getReportPdf("mob/Boletim_Jari");	
			customConnection.finishConnection();
			return print;
		}  catch(Exception ex) {
			managerLog.error("Erro! AtaService.getBoletimReport:", ex.getMessage());
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public byte[] printAta(Integer cdAta) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			Report report = generateReportsAta.printAta(cdAta, customConnection);
			byte[] print = report.getReportPdf("mob/resumo_ata_jari");
			customConnection.finishConnection();
			return print;			
		}  catch(Exception ex) {
			managerLog.error("Erro! AtaService.printAta:", ex.getMessage());
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public void dataJulgamentoValidate(AtaDTO ataDto, CustomConnection connection) throws Exception, ValidacaoException {
		GregorianCalendar dataAtual = new GregorianCalendar();
		dataAtual.set(Calendar.HOUR, 23);
		dataAtual.set(Calendar.MINUTE, 59);
		dataAtual.set(Calendar.SECOND, 59);
		Documento documento = documentoRepository.get(ataDto.getCdDocumento());
		if(ataDto.getDtAta().before(documento.getDtProtocolo())) {
			throw new ValidationException("A data do resultado não deve ser anterior a data do protocolo.");
		}
		if(ataDto.getDtAta().after(dataAtual)) {
			throw new ValidationException("A data do resultado não deve ser posterior a data atual.");
		}
	}
	
	@Override
	public Search<AtaDTO> findDocumentosAta(SearchCriterios searchCriterios) throws Exception {
		List<String> resultadoJari = new ArrayList<String>();
		resultadoJari.add(String.valueOf(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()));
		resultadoJari.add(String.valueOf(TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey()));
		searchCriterios.addCriterios("D.tp_status", resultadoJari.toString().replace("[", "").replace("]", ""), ItemComparator.IN, Types.STRING);
		Search<AtaDTO> search = new SearchBuilder<AtaDTO>("PTC_ATA A")
				.fields("E.id_ait, F.nr_documento AS nr_protocolo, D.dt_movimento AS dt_entrada_recurso, F.nm_requerente AS nm_solicitante, G.nm_situacao_documento")
				.addJoinTable("JOIN PTC_RECURSO B ON (A.cd_ata = B.cd_ata)")
				.addJoinTable("JOIN MOB_AIT_MOVIMENTO_DOCUMENTO C ON (B.CD_DOCUMENTO = C.CD_DOCUMENTO) ")
				.addJoinTable("JOIN MOB_AIT_MOVIMENTO D ON (C.CD_AIT = D.CD_AIT) ")
				.addJoinTable("JOIN MOB_AIT E ON (D.CD_AIT = E.CD_AIT)")
				.addJoinTable("JOIN PTC_DOCUMENTO F ON(F.CD_DOCUMENTO = B.CD_DOCUMENTO) ")
				.addJoinTable("LEFT OUTER JOIN PTC_SITUACAO_DOCUMENTO G ON(G.CD_SITUACAO_DOCUMENTO = F.CD_SITUACAO_DOCUMENTO)")
				.addJoinTable("LEFT OUTER JOIN SEG_USUARIO H ON(A.CD_USUARIO = H.CD_USUARIO)")
				.addJoinTable("LEFT OUTER JOIN GRL_PESSOA I ON(I.CD_PESSOA = H.CD_PESSOA)")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}
}
