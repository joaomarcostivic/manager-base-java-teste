package com.tivic.manager.mob.eventoequipamento;

import java.util.List;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.EventoEquipamentoDAO;
import com.tivic.manager.mob.eventoequipamento.enums.SituacaoEventoEnum;
import com.tivic.manager.mob.processamento.conversao.dtos.GrupoEventoDTO;
import com.tivic.manager.triagem.dtos.EventoTriagemDTO;
import com.tivic.manager.triagem.dtos.GrupoEventoParamsDTO;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class EventoEquipamentoRepositoryDAO implements EventoEquipamentoRepository {

	@Override
	public EventoEquipamento insert(EventoEquipamento eventoEquipamento, CustomConnection customConnection) throws Exception {
		int cdEventoEquipamento = EventoEquipamentoDAO.insert(eventoEquipamento, customConnection.getConnection());
		if (cdEventoEquipamento <= 0)
			throw new Exception("Erro ao inserir evento.");
		eventoEquipamento.setCdEvento(cdEventoEquipamento);	
		return eventoEquipamento;
	}

	@Override
	public EventoEquipamento update(EventoEquipamento eventoEquipamento, CustomConnection customConnection) throws Exception {
		int eventoEquipamentoUpdate = EventoEquipamentoDAO.update(eventoEquipamento, customConnection.getConnection());
		if(eventoEquipamentoUpdate <= 0)
			throw new Exception("Erro ao atualizar o evento");
		return eventoEquipamento;
	}
	
	@Override
	public EventoEquipamento get(int cdEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			EventoEquipamento eventoEstacionamentoDigital = get(cdEvento, customConnection);
			customConnection.finishConnection();
			return eventoEstacionamentoDigital;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public EventoEquipamento get(int cdEventoEquipamento, CustomConnection customConnection) throws Exception {
		return EventoEquipamentoDAO.get(cdEventoEquipamento, customConnection.getConnection());
	}

	@Override
	public List<EventoEquipamento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<EventoEquipamento> search = new SearchBuilder<EventoEquipamento>("mob_evento_equipamento")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(EventoEquipamento.class);
	}
	
	@Override
	public List<GrupoEventoParamsDTO> getGruposDeEventos(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("E.st_evento", SituacaoEventoEnum.ST_EVENTO_NAO_PROCESSADO.getKey());
		Search<GrupoEventoParamsDTO> search = new SearchBuilder<GrupoEventoParamsDTO>("mob_evento_equipamento E")
				.fields("ROW_NUMBER() OVER (ORDER BY CAST(E.dt_conclusao AS DATE), ORG.id_orgao) as cd_grupo_evento, "
						+ "ORG.cd_orgao, ORG.nm_orgao, E.nm_orgao_autuador as id_orgao, "
						+ "CID.cd_cidade, CID.nm_cidade, "
						+ "CAST(E.dt_conclusao AS DATE) as dt_grupo_evento, "
						+ "COUNT(*) as qt_eventos")
				.addJoinTable("INNER JOIN mob_evento_estacionamento_digital ED ON E.cd_evento = ED.cd_evento")
				.addJoinTable("INNER JOIN mob_orgao ORG ON ORG.lg_emitir_ait <> 0")
				.addJoinTable("INNER JOIN grl_cidade CID ON ORG.cd_cidade = CID.cd_cidade")
				.searchCriterios(searchCriterios)
				.groupBy("CAST(e.dt_conclusao AS DATE), org.cd_orgao, CID.cd_cidade, E.nm_orgao_autuador")
				.customConnection(customConnection)
				.build();
		return search.getList(GrupoEventoParamsDTO.class);
	}
	
	@Override
	public List<EventoTriagemDTO> findTriagem(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<EventoTriagemDTO> search = new SearchBuilder<EventoTriagemDTO>("mob_evento_equipamento")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(EventoTriagemDTO.class);
	}

	public Search<GrupoEventoDTO> getNaoEmitidosAgrupados(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.addCriteriosEqualInteger("st_evento", SituacaoEventoEnum.ST_EVENTO_CONFIRMADO.getKey());

		Search<GrupoEventoDTO> search = new SearchBuilder<GrupoEventoDTO>("mob_evento_equipamento E")
				.fields("ROW_NUMBER() OVER (ORDER BY CAST(E.dt_conclusao AS DATE)) as cd_grupo_evento, "
						+ "CAST(E.dt_conclusao AS DATE) as dt_grupo_evento, "
						+ "COUNT(*) as qtd_eventos")
				.searchCriterios(searchCriterios)
				.additionalCriterias("NOT EXISTS (SELECT * FROM mob_ait_evento AE WHERE E.cd_evento = AE.cd_evento)" +
						"AND cd_evento IN (SELECT cd_evento FROM mob_evento_arquivo EA, grl_arquivo A WHERE EA.cd_arquivo = A.cd_arquivo)")
				.groupBy("dt_grupo_evento ")
				.orderBy("dt_grupo_evento ASC")
				.customConnection(customConnection)
				.count()
				.build();

		return search;
	}
	
	public Search<EventoEquipamento> getNaoEmitidos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.addCriteriosEqualInteger("st_evento", SituacaoEventoEnum.ST_EVENTO_CONFIRMADO.getKey(), true);
		
		Search<EventoEquipamento> search = new SearchBuilder<EventoEquipamento>("mob_evento_equipamento A")
				.addJoinTable("JOIN mob_tipo_evento B ON (A.cd_tipo_evento = B.cd_tipo_evento)")
				.searchCriterios(searchCriterios)
				.additionalCriterias("B.id_tipo_evento IN ('ASV', 'VAL', 'PSF', 'RLP', 'CEX', 'LNP', 'LHP', 'CLP')" + 
						"AND NOT EXISTS (SELECT * FROM mob_ait_evento B WHERE A.cd_evento = B.cd_evento)" +
						"AND cd_evento IN (SELECT cd_evento FROM mob_evento_arquivo D, grl_arquivo sB WHERE D.cd_arquivo = sB.cd_arquivo)")
				.orderBy("A.dt_evento ASC")
				.count()
				.customConnection(customConnection)
				.build();

		return search;
	}
	
	@Override
	public List<EventoEquipamento> getDisponiveisSincronizacao(String nmOrgaoAutuador, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<EventoEquipamento> search = new SearchBuilder<EventoEquipamento>("mob_evento_equipamento A")
				.addJoinTable("JOIN mob_tipo_evento B ON (A.cd_tipo_evento = B.cd_tipo_evento)")
				.searchCriterios(searchCriterios)
				.additionalCriterias("B.id_tipo_evento IN ('VAL', 'ASV', 'PSF')" + 
						" AND A.st_evento <> " + SituacaoEventoEnum.ST_EVENTO_NAO_PROCESSADO.getKey() +
						" AND A.st_evento <> " + SituacaoEventoEnum.ST_EVENTO_CANCELADO.getKey() +
						" AND A.nm_orgao_autuador = '" + nmOrgaoAutuador + "'"+
						" AND (A.lg_enviado = 0 OR A.lg_enviado IS NULL)")
				.orderBy(" A.dt_evento ASC ")
				.customConnection(customConnection)
				.build();
		
		return search.getList(EventoEquipamento.class);
	}
}
