package com.tivic.manager.triagem.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ws.rs.core.NoContentException;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.eventoarquivo.EventoArquivoRepository;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;
import com.tivic.manager.triagem.repositories.IEventoEstacionamentoDigitalRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class EstacionamentoAppSyncService implements IEstacionamentoAppSyncService {
	
	private IParametroRepository parametroRepository;
	private IEventoEstacionamentoDigitalRepository eventoEstacionamentoDigitalRepository;
	private EventoArquivoRepository eventoArquivoRepository;
	private IArquivoRepository arquivoRepository;
	
	public EstacionamentoAppSyncService() throws Exception {
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		eventoEstacionamentoDigitalRepository = (IEventoEstacionamentoDigitalRepository) BeansFactory.get(BeansFactory.class);
		eventoArquivoRepository = (EventoArquivoRepository) BeansFactory.get(EventoArquivoRepository.class);
	}

	@Override
	public List<EstacionamentoDigitalAppResponse> findNotificacoesPendentes() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<EstacionamentoDigitalAppResponse> notificacoesPendentesList = searchNotificacoesPendentes(customConnection);
			GregorianCalendar prazoResposta = calcularPrazoResposta();
			for (EstacionamentoDigitalAppResponse notificacao : notificacoesPendentesList) {
				buscarImagensNotificacao(notificacao, customConnection);
				setarLimiteVerificacao(notificacao, prazoResposta, customConnection);
			}
			customConnection.finishConnection();
			return notificacoesPendentesList;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private List<EstacionamentoDigitalAppResponse> searchNotificacoesPendentes(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_tipo_evento", parametroRepository.getValorOfParametroAsInt("MOB_EVENTO_ESTACIONAMENTO_DIGITAL"));
		searchCriterios.setQtLimite(parametroRepository.getValorOfParametroAsInt("MOB_EVENTO_ESTACIONAMENTO_LIMIT_SYNC"));
		Search<EstacionamentoDigitalAppResponse> search = new SearchBuilder<EstacionamentoDigitalAppResponse>("mob_evento_equipamento A")
				.fields("A.*, B.*, B.dt_tempo_verificacao as dt_time_limit_response")
				.addJoinTable(" LEFT OUTER JOIN mob_evento_estacionamento_digital B ON (A.cd_evento = B.cd_evento)")
				.addJoinTable(" LEFT OUTER JOIN mob_evento_estacionamento_digital_detalhes C ON (A.cd_evento = C.cd_evento)")
				.searchCriterios(searchCriterios)
				.additionalCriterias (
					" NOT EXISTS "
				  + " ( "
				  + "	SELECT D.* FROM mob_ait_evento D WHERE D.cd_evento = A.cd_evento "
				  + " ) "
				  + " AND EXISTS"
				  + " ("
				  + "	SELECT E.cd_evento, E.dt_tempo_verificacao FROM mob_evento_estacionamento_digital E WHERE ("
				  + "		E.dt_tempo_verificacao is null OR E.dt_tempo_verificacao < CURRENT_TIMESTAMP "
				  + "	) AND E.cd_evento = A.cd_evento"
				  + " )"
				)
				.build();
		List<EstacionamentoDigitalAppResponse> notificacoesPendentesList = search.getList(EstacionamentoDigitalAppResponse.class);
		if (notificacoesPendentesList.isEmpty()) {
			throw new NoContentException("Nenhuma notificação pendente encontrada, tente novamente mais tarde.");
		}
		return notificacoesPendentesList;
	}
	
	private void buscarImagensNotificacao(EstacionamentoDigitalAppResponse notificacao, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_evento", notificacao.getCdEvento());
		List<EventoArquivo> eventoArquivoList = eventoArquivoRepository.find(searchCriterios, customConnection);
		if (!eventoArquivoList.isEmpty()) {
			List<byte[]> imagensList = new ArrayList<byte[]>();
			for (EventoArquivo eventoArquivo : eventoArquivoList) {
				Arquivo arquivo = arquivoRepository.get(eventoArquivo.getCdArquivo(), customConnection);
				imagensList.add(arquivo.getBlbArquivo());
			}
			notificacao.setImagens(imagensList);
		}
	}
	
	private GregorianCalendar calcularPrazoResposta() throws Exception {
		int tempoLimite = parametroRepository.getValorOfParametroAsInt("MOB_EVENTO_ESTACIONAMENTO_TEMPO_LIMIT");
		GregorianCalendar prazoResposta = new GregorianCalendar();
		prazoResposta.add(Calendar.HOUR_OF_DAY, tempoLimite);
		return prazoResposta;
	}
	
	private void setarLimiteVerificacao(EstacionamentoDigitalAppResponse notificacao, GregorianCalendar prazoResposta, CustomConnection customConnection) throws Exception {
		notificacao.setDtTimeLimitResponse(prazoResposta);
		EventoEstacionamentoDigital eventoEstacionamentoDigital = eventoEstacionamentoDigitalRepository.get(notificacao.getCdEvento(), customConnection);
		eventoEstacionamentoDigital.setDtTempoVerificacao(prazoResposta);
		eventoEstacionamentoDigitalRepository.update(eventoEstacionamentoDigital, customConnection);
	}

}
	