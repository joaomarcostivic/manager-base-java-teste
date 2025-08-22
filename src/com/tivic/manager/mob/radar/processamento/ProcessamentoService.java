package com.tivic.manager.mob.radar.processamento;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.eventoarquivo.IEventoArquivoService;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.manager.mob.eventoequipamento.enums.SituacaoEventoEnum;
import com.tivic.manager.util.radar.BancoRadarConnection;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ProcessamentoService implements IProcessamentoService {

	private BancoRadarConnection bancoRadarConnection;
	private IParametroRepository parametroRepository;
	private EventoEquipamentoRepository eventoEquipamentoRepository;
	private IEventoArquivoService eventoArquivoService;

	public ProcessamentoService() throws Exception {
		this.bancoRadarConnection = new BancoRadarConnection();
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
		this.eventoArquivoService = (IEventoArquivoService) BeansFactory.get(IEventoArquivoService.class);
	}
	
	public ProcessamentoEventosDTO processarEventos() throws Exception {
		CustomConnection customConnectionRadar = new CustomConnection();
		try {
			customConnectionRadar.initConnection(true, bancoRadarConnection.getConnection());
			ProcessamentoEventosDTO processamentoEventosDTO = processarEventos(customConnectionRadar);
			customConnectionRadar.finishConnection();
			return processamentoEventosDTO;
		} finally {
			customConnectionRadar.closeConnection();
		}
	}
	
	public ProcessamentoEventosDTO processarEventos(CustomConnection customConnectionRadar) throws Exception {
		ProcessamentoEventosDTO processamentoEventosDTO = new ProcessamentoEventosDTO();
		
		for(EventoEquipamento evento: getEventosNaoProcessados(customConnectionRadar)) {
			try {
				if(evento.getNrPlaca() == null || evento.getNrPlaca() == "") {
					processamentoEventosDTO.addEventoNaoProcessado(evento, "Placa inválida\n");
					continue;
				}
				
				confirmarEvento(evento);
	            eventoEquipamentoRepository.update(evento, customConnectionRadar);
	            	    		
	            List<EventoArquivo> eventoArquivos = eventoArquivoService.find(getEventosArquivoCriterios(evento.getCdEvento()), customConnectionRadar);
	            if(!eventoArquivos.isEmpty()) {
	            	EventoArquivo eventoArquivo = eventoArquivos.get(0);
	            	eventoArquivo.setLgImpressao(1);
	            	eventoArquivoService.update(eventoArquivo, customConnectionRadar);
	            }
	            
	            processamentoEventosDTO.addEventoProcessado();
			} catch(Exception e) {
				processamentoEventosDTO.addEventoNaoProcessado(evento, e.getMessage());
				continue;
			}
		}
		
		return processamentoEventosDTO;
	}
	
	private List<EventoEquipamento> getEventosNaoProcessados(CustomConnection customConnectionRadar) throws Exception {
		Search<EventoEquipamento> search = new SearchBuilder<EventoEquipamento>("mob_evento_equipamento A")
				.searchCriterios(getEventosNaoProcessadosCriterios())
				.orderBy("A.dt_evento ASC")
				.count()
				.customConnection(customConnectionRadar)
				.build();
		
		return search.getList(EventoEquipamento.class);
	}
	
	private void confirmarEvento(EventoEquipamento evento) throws Exception {
        evento.setCdUsuarioConfirmacao(this.parametroRepository.getValorOfParametroAsInt("MOB_USER_TIVIC"));
        evento.setStEvento(SituacaoEventoEnum.ST_EVENTO_CONFIRMADO.getKey());
        evento.setDtProcessamento(new GregorianCalendar());
	}
	
	private SearchCriterios getEventosArquivoCriterios(int cdEvento) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_evento", cdEvento);
		
		return searchCriterios;
	}
	
	private SearchCriterios getEventosNaoProcessadosCriterios() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("st_evento", SituacaoEventoEnum.ST_EVENTO_NAO_PROCESSADO.getKey());
		searchCriterios.addCriteriosEqualString("nm_orgao_autuador", bancoRadarConnection.getNmOrgaoAutuador());
		
		return searchCriterios;
	}

}