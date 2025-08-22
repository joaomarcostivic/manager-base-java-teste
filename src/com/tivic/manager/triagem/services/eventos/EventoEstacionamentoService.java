package com.tivic.manager.triagem.services.eventos;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.mob.eventoarquivo.EventoArquivoRepository;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.mob.processamento.conversao.services.IConversorEventoService;
import com.tivic.manager.mob.talonario.ITalonarioService;
import com.tivic.manager.mob.tipoevento.TipoEventoRepository;
import com.tivic.manager.triagem.builders.ArquivoTriagemEstacionamentoBuilder;
import com.tivic.manager.triagem.builders.EquipamentoTriagemBuilder;
import com.tivic.manager.triagem.builders.EventoArquivoTriagemBuilder;
import com.tivic.manager.triagem.builders.EventoEquipamentoTriagemBuilder;
import com.tivic.manager.triagem.builders.EventoEstacionamentoDetalhesNotificacaoBuilder;
import com.tivic.manager.triagem.builders.EventoEstacionamentoNotificacaoBuilder;
import com.tivic.manager.triagem.builders.EventoImagemNotificacaoBuilder;
import com.tivic.manager.triagem.builders.EventoTriagemBuilder;
import com.tivic.manager.triagem.builders.NotificacaoEstacionamentoDigitalBuilder;
import com.tivic.manager.triagem.builders.OrgaoTriagemBuilder;
import com.tivic.manager.triagem.dtos.EquipamentoTriagemDTO;
import com.tivic.manager.triagem.dtos.EventoTriagemDTO;
import com.tivic.manager.triagem.dtos.ImagemNotificacaoDTO;
import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;
import com.tivic.manager.triagem.dtos.OrgaoTriagemDTO;
import com.tivic.manager.triagem.dtos.ProcessamentoEventoDTO;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalDetalhes;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalImagem;
import com.tivic.manager.triagem.repositories.IEstacionamentoDigitalImagemRepository;
import com.tivic.manager.triagem.repositories.IEventoEstacionamentoDigitalDetalhesRepository;
import com.tivic.manager.triagem.repositories.IEventoEstacionamentoDigitalRepository;
import com.tivic.manager.triagem.services.eventos.validator.CapturaEventoEstacionamentoValidationStatistics;
import com.tivic.manager.triagem.services.eventos.validator.CapturaEventoEstacionamentoValidatorBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchCriterios;

public class EventoEstacionamentoService implements IEventoEstacionamentoService {
	
	private ManagerLog managerLog;
	private EventoEquipamentoRepository eventoEquipamentoRepository;
	private IEventoEstacionamentoDigitalRepository eventoEstacionamentoDigitalRepository;
	private IEventoEstacionamentoDigitalDetalhesRepository estacionamentoDigitalDetalhesRepository;
	private IEstacionamentoDigitalImagemRepository estacionamentoDigitalImagemRepository;
	private IArquivoRepository arquivoRepository;
	private EventoArquivoRepository eventoArquivoRepository;
	private TipoEventoRepository tipoEventoRepository;
	private IParametroRepository parametroRepository;
	private IOrgaoService orgaoService;
	private IConversorEventoService conversorEventoHandlerService;
	private ITalonarioService talonarioService;
	private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
	
	public EventoEstacionamentoService() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
		eventoEstacionamentoDigitalRepository = (IEventoEstacionamentoDigitalRepository) BeansFactory.get(IEventoEstacionamentoDigitalRepository.class);
		estacionamentoDigitalDetalhesRepository = (IEventoEstacionamentoDigitalDetalhesRepository) BeansFactory.get(IEventoEstacionamentoDigitalDetalhesRepository.class);
		estacionamentoDigitalImagemRepository = (IEstacionamentoDigitalImagemRepository) BeansFactory.get(IEstacionamentoDigitalImagemRepository.class);
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		eventoArquivoRepository = (EventoArquivoRepository) BeansFactory.get(EventoArquivoRepository.class);
		tipoEventoRepository = (TipoEventoRepository) BeansFactory.get(TipoEventoRepository.class);
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		conversorEventoHandlerService = (IConversorEventoService) BeansFactory.get(IConversorEventoService.class);
		talonarioService = (ITalonarioService) BeansFactory.get(ITalonarioService.class);
	    conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
	}

	public void salvar(List<NotificacaoEstacionamentoDigitalDTO> notificacaoEstacionamentoDigitalList) throws Exception {
	    CapturaEventoEstacionamentoValidationStatistics stats = new CapturaEventoEstacionamentoValidationStatistics();
	    CustomConnection customConnection = new CustomConnection();
	    
	    try {
	        customConnection.initConnection(true);
	        for (NotificacaoEstacionamentoDigitalDTO notificacao : notificacaoEstacionamentoDigitalList) {
	            stats.incrementTotalProcessed();
	            
	            if (!processNotification(notificacao, stats, customConnection)) {
	                continue;
	            }
	            
	            stats.incrementSuccessfullyPersisted();
	        }
	    } finally {
	        customConnection.closeConnection();
	        managerLog.info("CAPTURA EVENTOS ZONA AZUL", stats.generateReport());
	    }
	}
	
	private boolean processNotification(NotificacaoEstacionamentoDigitalDTO notificacao, 
			CapturaEventoEstacionamentoValidationStatistics stats, CustomConnection customConnection) throws Exception {
		
		CapturaEventoEstacionamentoValidatorBuilder validator = new CapturaEventoEstacionamentoValidatorBuilder();

		if (!validator.validate(notificacao, stats)) 
			return false;
		
        EventoEquipamento eventoEquipamento = new EventoEquipamentoTriagemBuilder()
                .notificacaoEstacionamentoDigital(notificacao)
                .build();
        eventoEquipamentoRepository.insert(eventoEquipamento, customConnection);

        EventoEstacionamentoDigital eventoEstacionamentoDigital = new EventoEstacionamentoNotificacaoBuilder()
                .notificacaoEstacionamentoDigital(notificacao, eventoEquipamento.getCdEvento())
                .build();
        eventoEstacionamentoDigitalRepository.insert(eventoEstacionamentoDigital, customConnection);

        EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes = new EventoEstacionamentoDetalhesNotificacaoBuilder()
                .notificacaoEstacionamentoDigital(notificacao, eventoEquipamento.getCdEvento())
                .build();
        estacionamentoDigitalDetalhesRepository.insert(eventoEstacionamentoDigitalDetalhes, customConnection);

        salvarImagensNotificacao(notificacao, eventoEquipamento.getCdEvento(), customConnection);
        customConnection.finishConnection();
        
		return true;
	}
	
	private void salvarImagensNotificacao(NotificacaoEstacionamentoDigitalDTO notificacao, int cdEvento, CustomConnection customConnection) throws Exception {
		for (ImagemNotificacaoDTO imagemNotificacaoDTO : notificacao.getImagemNotificacaoDTOList()) {
			
			if (isImagemDuplicada(cdEvento, imagemNotificacaoDTO.getUrl(), customConnection)) {
				continue;
			}
			
			EventoEstacionamentoDigitalImagem eventoEstacionamentoDigitalImagem = new EventoImagemNotificacaoBuilder()
					.imagemNotificacao(imagemNotificacaoDTO, cdEvento)
					.build();
			estacionamentoDigitalImagemRepository.insert(eventoEstacionamentoDigitalImagem, customConnection);
			Arquivo arquivo = new ArquivoTriagemEstacionamentoBuilder()
					.notificacao(notificacao)
					.arquivo(imagemNotificacaoDTO.getBlbImagem())
					.build();
			arquivoRepository.insert(arquivo, customConnection);
			EventoArquivo eventoArquivo = new EventoArquivoTriagemBuilder()
					.cdEvento(cdEvento)
					.cdArquivo(arquivo.getCdArquivo())
					.dtAquivo(notificacao.getDtNotificacao())
					.build();
			eventoArquivoRepository.insert(eventoArquivo, customConnection);
		}
	}
	
	@Override
	public List<EventoTriagemDTO> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<EventoTriagemDTO> eventos = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return eventos;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<EventoTriagemDTO> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<EventoTriagemDTO> eventos = eventoEquipamentoRepository.findTriagem(searchCriterios, customConnection);
		TipoEvento tipoEvento = tipoEventoRepository.get(getParametroValue("MOB_EVENTO_ESTACIONAMENTO_DIGITAL"), customConnection);
		
		eventos.forEach(evento -> evento.setTipoEvento(tipoEvento));
		
		return eventos;
	}
	
	@Override
	public EventoTriagemDTO get(int cdEvento, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			EventoTriagemDTO evento = get(cdEvento, cdUsuario, customConnection);
			customConnection.finishConnection();
			return evento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public EventoTriagemDTO get(int cdEvento, int cdUsuario, CustomConnection customConnection) throws Exception {
		EventoEquipamento evento = eventoEquipamentoRepository.get(cdEvento, customConnection);
		Agente agente = conversorBaseAntigaNovaFactory.getAgenteRepository().getByCdUsuario(cdUsuario, customConnection);
		
		if(evento == null)	
			throw new NoContentException("Nenhum evento encontrado");
		
		TipoEvento tipoEvento = tipoEventoRepository.get(getParametroValue("MOB_EVENTO_ESTACIONAMENTO_DIGITAL"), customConnection);
		OrgaoTriagemDTO orgaoTriagem = getOrgaoTriagem();
		EquipamentoTriagemDTO equipamento = new EquipamentoTriagemBuilder()
				.cdEquipamento(getParametroValue("CD_EQUIPAMENTO_ESTACIONAMENTO_DIGITAL"))
				.orgaoTriagem(orgaoTriagem)
				.build();
		
		talonarioService.getByCdEquipamento(agente.getCdAgente(), equipamento.getCdEquipamento());
		
		return buildEventoTriagem(evento, tipoEvento, equipamento);
	}
	
	private OrgaoTriagemDTO getOrgaoTriagem() throws Exception {
		Orgao orgao = orgaoService.getOrgaoUnico();
		
		return new OrgaoTriagemBuilder()
				.cdOrgao(orgao.getCdOrgao())
				.nmOrgao(orgao.getNmOrgao())
				.idOrgao(orgao.getIdOrgao())
				.cdAgenteResponsavel(orgao.getCdAgenteResponsavel())
				.cdOrgaoAutuador(conversorBaseAntigaNovaFactory.getParametroRepository().getOrgaoAutuanteParamValue())
				.build();
	}
	
	private EventoTriagemDTO buildEventoTriagem(EventoEquipamento evento, TipoEvento tipoEvento, EquipamentoTriagemDTO equipamento) {
		return new EventoTriagemBuilder()
				.cdEvento(evento.getCdEvento())
				.stEvento(evento.getStEvento())
				.tipoEvento(tipoEvento)
				.dtEvento(evento.getDtEvento())
				.dtConclusao(evento.getDtConclusao())
				.dsLocal(evento.getDsLocal())
				.nmOrgaoAutuador(evento.getNmOrgaoAutuador())
				.nrPlaca(evento.getNrPlaca())
				.equipamento(equipamento)
				.build();
	}
	
	@Override
	public void confirmar(ProcessamentoEventoDTO processamentoEventoDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			confirmar(processamentoEventoDTO, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void confirmar(ProcessamentoEventoDTO processamentoEventoDTO, CustomConnection customConnection) throws Exception {
		EventoEquipamento evento = eventoEquipamentoRepository.get(processamentoEventoDTO.getCdEvento(), customConnection);
		EventoEstacionamentoDigitalDetalhes detalhes = estacionamentoDigitalDetalhesRepository.getByCdEvento(processamentoEventoDTO.getCdEvento(), customConnection);
		evento.setCdUsuarioConfirmacao(processamentoEventoDTO.getCdUsuarioConfirmacao());
		evento.setStEvento(processamentoEventoDTO.getStEvento());
		evento.setNrPlaca(processamentoEventoDTO.getNrPlaca());
		evento.setCdEquipamento(getParametroValue("CD_EQUIPAMENTO_ESTACIONAMENTO_DIGITAL"));
		if (detalhes != null && detalhes.getNmRuaNotificacao() != null && detalhes.getNrImovelReferencia() != null)
			evento.setDsLocal(detalhes.getNmRuaNotificacao() + ", " + detalhes.getNrImovelReferencia());
		
		this.eventoEquipamentoRepository.update(evento, customConnection);
		processarEvento(evento, processamentoEventoDTO.getCdUsuarioConfirmacao(), customConnection);
	}
	
	private void processarEvento(EventoEquipamento evento, int cdUsuario, CustomConnection customConnection) throws Exception {
		Orgao orgao = orgaoService.getOrgaoUnico();
		try {
			conversorEventoHandlerService.convert(evento, orgao, cdUsuario, customConnection);
		} catch (Exception e) {
			managerLog.showLog(e);
			managerLog.info("Evento: " + evento.getCdEvento(), e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public void rejeitar(ProcessamentoEventoDTO processamentoEventoDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			rejeitar(processamentoEventoDTO, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void rejeitar(ProcessamentoEventoDTO processamentoEventoDTO, CustomConnection customConnection) throws Exception {
		EventoEquipamento evento = this.eventoEquipamentoRepository.get(processamentoEventoDTO.getCdEvento(), customConnection);
		EventoEstacionamentoDigital eventoEstacionamento = this.eventoEstacionamentoDigitalRepository.get(evento.getCdEvento(), customConnection);
		evento.setCdUsuarioCancelamento(processamentoEventoDTO.getCdUsuarioCancelamento());
		evento.setStEvento(processamentoEventoDTO.getStEvento());
		evento.setCdMotivoCancelamento(processamentoEventoDTO.getCdMotivoCancelamento());
		eventoEstacionamento.setDsMotivoCancelamento(processamentoEventoDTO.getDsObservacaoCancelamento());
		
		this.eventoEquipamentoRepository.update(evento, customConnection);
		this.eventoEstacionamentoDigitalRepository.update(eventoEstacionamento, customConnection);
	}
	
	@Override
	public NotificacaoEstacionamentoDigitalDTO getNotificacaoByEvento(int cdEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			NotificacaoEstacionamentoDigitalDTO notificacao = getNotificacaoByEvento(cdEvento, customConnection);
			customConnection.finishConnection();
			return notificacao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public NotificacaoEstacionamentoDigitalDTO getNotificacaoByEvento(int cdEvento, CustomConnection customConnection) throws Exception {
		EventoEquipamento evento = eventoEquipamentoRepository.get(cdEvento, customConnection);
		EventoEstacionamentoDigital eventoEstacionamento = eventoEstacionamentoDigitalRepository.get(cdEvento, customConnection);
		EventoEstacionamentoDigitalDetalhes detalhes = estacionamentoDigitalDetalhesRepository.getByCdEvento(cdEvento, customConnection);
		
		return new NotificacaoEstacionamentoDigitalBuilder()
				.nmOrgaoAutuador(evento.getNmOrgaoAutuador())
				.dtNotificacao(evento.getDtConclusao())
				.nrNotificacao(eventoEstacionamento.getNrNotificacao())
				.dsNotificacao(eventoEstacionamento.getDsNotificacao())
				.idDispositivo(detalhes.getIdDispositivo())
				.nrVaga(detalhes.getNrVaga())
				.nmRuaNotificacao(detalhes.getNmRuaNotificacao())
				.nrImovelReferencia(detalhes.getNrImovelReferencia())
				.idColaborador(detalhes.getIdColaborador())
				.build();
	}
	
	private int getParametroValue(String nmParametro) throws Exception {
		int vlParametro = parametroRepository.getValorOfParametroAsInt(nmParametro);
		
		if(vlParametro == 0)
			throw new IllegalArgumentException("O parâmetro " + nmParametro + " não foi configurado.");
		
		return vlParametro;
	}
	
	private boolean isImagemDuplicada(int cdEvento, String url, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("url_imagem", url);
		searchCriterios.addCriteriosEqualInteger("cd_evento", cdEvento);
		List<EventoEstacionamentoDigitalImagem> eventoImagens = estacionamentoDigitalImagemRepository.find(searchCriterios, customConnection);
		
		if (!eventoImagens.isEmpty()) {
	        return true;
		}
		return false;
	}
}
