package com.tivic.manager.mob.processamento.sincronizacao.services;

import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.IEquipamentoService;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.mob.eventoarquivo.EventoArquivoBuilder;
import com.tivic.manager.mob.eventoarquivo.IEventoArquivoService;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.manager.mob.eventoequipamento.enums.LgEnviadoEnum;
import com.tivic.manager.mob.processamento.sincronizacao.builders.EventoSyncBuilder;
import com.tivic.manager.mob.processamento.sincronizacao.factories.CriterioFactory;
import com.tivic.manager.mob.tipoevento.ITipoEventoService;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.radar.BancoRadarConnection;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class EventoRadarHandlerService implements IEventoRadarHandlerService {
	
	private BancoRadarConnection bancoRadarConnection;
    private ManagerLog managerLog;
	private IEquipamentoService equipamentoService;
	private ITipoEventoService tipoEventoService;
	private IEventoArquivoService eventoArquivoService;
	private IArquivoService arquivoService;
	private EventoEquipamentoRepository eventoEquipamentoRepository;
    
    public EventoRadarHandlerService() throws Exception {
    	this.bancoRadarConnection = new BancoRadarConnection();
        this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.equipamentoService = (IEquipamentoService) BeansFactory.get(IEquipamentoService.class);
		this.tipoEventoService = (ITipoEventoService) BeansFactory.get(ITipoEventoService.class);
		this.eventoArquivoService = (IEventoArquivoService) BeansFactory.get(IEventoArquivoService.class);
		this.arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		this.eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
    }
    
    @Override
	public void add(EventoEquipamento eventoRadar) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		CustomConnection customConnectionRadar = new CustomConnection();
		try {
			customConnection.initConnection(true);
			customConnectionRadar.initConnection(true, bancoRadarConnection.getConnection());
			add(eventoRadar, customConnection, customConnectionRadar);
			customConnection.finishConnection();
			customConnectionRadar.finishConnection();
		} catch (Exception e) {
			managerLog.showLog(e);
			throw new Exception(e.getMessage());
		} finally {
			customConnection.closeConnection();
			customConnectionRadar.closeConnection();
		}
	}
    
    @Override
    public void add(EventoEquipamento eventoRadar, CustomConnection customConnection, CustomConnection customConnectionRadar) throws Exception {
    	managerLog.info("Sincronização de eventos", " Sincronizando do radar o evento: " + eventoRadar.getCdEvento());
    	if (!hasImagens(eventoRadar.getCdEvento(), customConnectionRadar)) {
    		throw new Exception("Evento não possui imagens! " + eventoRadar.getCdEvento());
    	}
    	Equipamento equipamento = equipamentoService.getEquipamentoAtivo(montarCriteriosEquipamento(eventoRadar), customConnection);
    	TipoEvento tipoEvento = getTipoEvento(eventoRadar.getCdTipoEvento(), customConnection, customConnectionRadar);
    	if (isEventoDuplicado(eventoRadar, customConnection)) {
    		throw new Exception("Evento possivelmente duplicado!");
    	}
        EventoEquipamento evento = eventoEquipamentoRepository.insert(new EventoSyncBuilder(eventoRadar, equipamento, tipoEvento).build(), customConnection);
        updateLgEnviadoEventoRadar(eventoRadar, customConnectionRadar);
        handleArquivosRadar(eventoRadar.getCdEvento(), evento.getCdEvento(), customConnection, customConnectionRadar);
        managerLog.info("Sincronização de eventos", "Evento sincronizado com sucesso: " + evento.getCdEvento());
    }

    private SearchCriterios montarCriteriosEquipamento(EventoEquipamento eventoEquipamento) {
    	SearchCriterios searchCriterios = new SearchCriterios();
    	searchCriterios = new CriterioFactory(searchCriterios).getStrategy().montarCriterios(eventoEquipamento);
    	return searchCriterios;
    }
	
	private boolean hasImagens(int cdEventoRadar, CustomConnection customConnectionRadar) throws Exception {
		List<EventoArquivo> eventoArquivosRadar = getEventoArquivosRadar(cdEventoRadar, customConnectionRadar);
		return !eventoArquivosRadar.isEmpty();
	}
	
	private void updateLgEnviadoEventoRadar(EventoEquipamento eventoRadar, CustomConnection customConnectionRadar) throws Exception {
		eventoRadar.setLgEnviado(LgEnviadoEnum.ENVIADO.getKey());
		this.eventoEquipamentoRepository.update(eventoRadar, customConnectionRadar);
	}
	
	private void handleArquivosRadar(int cdEventoRadar, int cdEvento, CustomConnection customConnection, CustomConnection customConnectionRadar) throws Exception {
		List<EventoArquivo> eventoArquivosRadar = getEventoArquivosRadar(cdEventoRadar, customConnectionRadar);
	    boolean isEventoArquivosValid = eventoArquivosRadar.stream().anyMatch(eventoArquivo -> eventoArquivo.getLgImpressao() > 0);
	    if (eventoArquivosRadar.size() > 1 && !isEventoArquivosValid) {
	    	throw new Exception("Nenhuma das imagens do evento estão elegíveis para impressão.");
	    }
		for(EventoArquivo eventoArquivoRadar : eventoArquivosRadar) {
			Arquivo arquivoRadar = this.arquivoService.get(eventoArquivoRadar.getCdArquivo(), customConnectionRadar);
			resetArquivo(arquivoRadar);
			this.arquivoService.save(arquivoRadar, customConnection);
			this.eventoArquivoService.insert(buildEventoArquivo(cdEvento, arquivoRadar.getCdArquivo(), eventoArquivoRadar), customConnection);
		}
	}
	
	private void resetArquivo(Arquivo arquivoRadar) {
		arquivoRadar.setCdArquivo(0);
	}
	
	private EventoArquivo buildEventoArquivo(int cdEvento, int cdArquivo, EventoArquivo eventoArquivoRadar) {
		return new EventoArquivoBuilder()
				.cdEvento(cdEvento)
				.cdArquivo(cdArquivo)
				.tpArquivo(eventoArquivoRadar.getTpArquivo())
				.idArquivo(eventoArquivoRadar.getIdArquivo())
				.tpEventoFoto(eventoArquivoRadar.getTpEventoFoto())
				.tpFoto(eventoArquivoRadar.getTpFoto())
				.dtArquivo(eventoArquivoRadar.getDtArquivo())
				.lgImpressao(eventoArquivoRadar.getLgImpressao())
				.build();
	}
	
	private List<EventoArquivo> getEventoArquivosRadar(int cdEventoRadar, CustomConnection customConnectionRadar) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_evento", cdEventoRadar);
		return eventoArquivoService.find(searchCriterios, customConnectionRadar);
	}
	
	private TipoEvento getTipoEvento(int cdTipoEvento, CustomConnection customConnection, CustomConnection customConnectionRadar) throws Exception {
		TipoEvento tipoEventoRadar = tipoEventoService.get(cdTipoEvento, customConnectionRadar);
		return tipoEventoService.getByIdTipoEvento(tipoEventoRadar.getIdTipoEvento(), customConnection);
	}
	
	private boolean isEventoDuplicado(EventoEquipamento evento, CustomConnection customConnection) throws Exception {
		try {
			GregorianCalendar dtConclusaoMinutoMais = (GregorianCalendar) evento.getDtConclusao().clone();
			dtConclusaoMinutoMais.add(Calendar.MINUTE, 1);
			GregorianCalendar dtConclusaoMinutoMenos = (GregorianCalendar) evento.getDtConclusao().clone();
			dtConclusaoMinutoMenos.add(Calendar.MINUTE, -1);
			List<EventoEquipamento> eventosDuplicados = eventoEquipamentoRepository.find(getEventoDuplicadoCriterios(evento, dtConclusaoMinutoMais, dtConclusaoMinutoMenos), customConnection);
			return !eventosDuplicados.isEmpty();
		} catch(Exception e) {
			throw new Exception("Erro ao verificar a duplicidade do evento.");
		}
	}
	
	private SearchCriterios getEventoDuplicadoCriterios(EventoEquipamento evento, GregorianCalendar dtConclusaoMinutoMais, GregorianCalendar dtConclusaoMinutoMenos) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nr_placa", evento.getNrPlaca());
		searchCriterios.addCriteriosEqualString("nm_equipamento", evento.getNmEquipamento());
		searchCriterios.addCriterios("dt_conclusao", Util.formatDate(dtConclusaoMinutoMenos, "yyyy-MM-dd HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.VARCHAR);
		searchCriterios.addCriterios("dt_conclusao", Util.formatDate(dtConclusaoMinutoMais, "yyyy-MM-dd HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.VARCHAR);
		return searchCriterios;
	}
}
