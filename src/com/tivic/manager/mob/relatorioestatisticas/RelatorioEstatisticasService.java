package com.tivic.manager.mob.relatorioestatisticas;

import java.sql.Types;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class RelatorioEstatisticasService implements IRelatorioEstatisticasService {

	@Override
	public PagedResponse<RelatorioEstatisticasDTO> findNais(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			int tpConsulta = Integer.parseInt((searchCriterios.getAndRemoveCriterio("tp_consulta")).getValue());
			customConnection.initConnection(false);
			
			Search<RelatorioEstatisticasDTO> restituicaoSearch = selectTpConsulta(searchCriterios, customConnection, tpConsulta);
			customConnection.finishConnection();
			return new PagedResponse<RelatorioEstatisticasDTO>(restituicaoSearch.getList(RelatorioEstatisticasDTO.class), restituicaoSearch.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Search<RelatorioEstatisticasDTO> selectTpConsulta(SearchCriterios searchCriterios, CustomConnection customConnection, int tpConsulta) throws Exception {
		if (tpConsulta != 1 && tpConsulta != 2) return null;
		return tpConsulta == 1 ?  findEntradasNa(searchCriterios, customConnection) : findJulgamentoDefesa(searchCriterios, customConnection);
	}
	
	public Search<RelatorioEstatisticasDTO> findEntradasNa(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		String periodicidade = searchCriterios.getAndRemoveCriterio("periodicidade").getValue();
		String aggregate = searchCriterios.getAndRemoveCriterio("aggregate_function").getValue();
		searchCriterios.addCriterios("A.tp_status", TipoStatusEnum.NAI_ENVIADO.getKey() + ", " + TipoStatusEnum.DEFESA_PREVIA.getKey() , ItemComparator.IN, Types.INTEGER);
		Search<RelatorioEstatisticasDTO> search = new SearchBuilder<RelatorioEstatisticasDTO>("mob_ait_movimento A")
				.fields(periodicidade + ", COUNT(A.cd_movimento) AS qt_notificacao, "
						+ "COUNT (CASE WHEN A.tp_status = " + TipoStatusEnum.DEFESA_PREVIA.getKey() + " THEN A.cd_movimento END) AS qt_recorrida ")
				.searchCriterios(searchCriterios)
				.groupBy(aggregate)
				.orderBy(aggregate)
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	public Search<RelatorioEstatisticasDTO> findJulgamentoDefesa(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		String periodicidade = searchCriterios.getAndRemoveCriterio("periodicidade").getValue();
		String aggregate = searchCriterios.getAndRemoveCriterio("aggregate_function").getValue();
		searchCriterios.addCriterios("A.tp_status", TipoStatusEnum.DEFESA_DEFERIDA.getKey() + ", " + TipoStatusEnum.DEFESA_INDEFERIDA.getKey() , ItemComparator.IN, Types.INTEGER);
		searchCriterios.addCriteriosEqualInteger("B.tp_status", TipoStatusEnum.DEFESA_PREVIA.getKey());
		searchCriterios.addCriteriosEqualInteger("B.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey());
		Search<RelatorioEstatisticasDTO> search = new SearchBuilder<RelatorioEstatisticasDTO>("mob_ait_movimento A")
				.fields(periodicidade + ", COUNT(*) AS qt_notificacao, COUNT(case when A.tp_status"
						+ " = " + TipoStatusEnum.DEFESA_DEFERIDA.getKey() + " THEN "
						+ "A.cd_movimento END) AS qt_deferimento, COUNT(CASE WHEN A.tp_status"
						+ " = " + TipoStatusEnum.DEFESA_INDEFERIDA.getKey() + " THEN "
						+ "A.cd_movimento END) AS qt_indeferimento, (COUNT(CASE WHEN A.tp_status"
						+ " = " + TipoStatusEnum.DEFESA_DEFERIDA.getKey() + " THEN "
						+ "'*' end)::float / COUNT(*)::float * 100) AS pr_deferimento, (COUNT(CASE WHEN A.tp_status"
						+ " = " + TipoStatusEnum.DEFESA_INDEFERIDA.getKey() + " THEN "
						+ "'*' end)::float / COUNT(*)::float * 100) AS "
						+ "pr_indeferimento, EXTRACT(days FROM AVG(A.dt_movimento - B.dt_movimento)) AS tempo_julgamento")
				.searchCriterios(searchCriterios)
				.addJoinTable("JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
				.groupBy(aggregate)
				.orderBy(aggregate)
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}

	@Override
	public PagedResponse<RelatorioEstatisticasNipDTO> findNips(SearchCriterios searchCriterios) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(false);
	        Search<RelatorioEstatisticasNipDTO> search = searchNips(searchCriterios, customConnection);
	        customConnection.finishConnection();
	        return new PagedResponse<RelatorioEstatisticasNipDTO>(search.getList(RelatorioEstatisticasNipDTO.class), search.getRsm().getTotal());
	    } finally {
	        customConnection.closeConnection();
	    }
	}

	public Search<RelatorioEstatisticasNipDTO> searchNips(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		String periodicidade = searchCriterios.getAndRemoveCriterio("periodicidade").getValue();
		String aggregate = searchCriterios.getAndRemoveCriterio("aggregate_function").getValue();
		searchCriterios.addCriterios("A.tp_status", TipoStatusEnum.RECURSO_JARI.getKey() + ", " + TipoStatusEnum.NIP_ENVIADA.getKey() , ItemComparator.IN, Types.INTEGER);
		Search<RelatorioEstatisticasNipDTO> search = new SearchBuilder<RelatorioEstatisticasNipDTO>("mob_ait_movimento A")
				.fields(periodicidade + ", COUNT (A.cd_movimento) AS qt_notificacao, "
						+ "SUM (CASE WHEN A.tp_status = " + TipoStatusEnum.RECURSO_JARI.getKey() + " THEN 1 ELSE 0 END) AS qt_recorrida")
				.searchCriterios(searchCriterios)
				.groupBy(aggregate)
				.orderBy(aggregate)
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	@Override
	public PagedResponse<RelatorioEstatisticasNipDTO> findJulgamentoJari(SearchCriterios searchCriterios) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(false);
	        Search<RelatorioEstatisticasNipDTO> search = searchJulgamentoJari(searchCriterios, customConnection);
	        customConnection.finishConnection();
	        return new PagedResponse<RelatorioEstatisticasNipDTO>(search.getList(RelatorioEstatisticasNipDTO.class), search.getRsm().getTotal());
	    } finally {
	        customConnection.closeConnection();
	    }
	}

	public Search<RelatorioEstatisticasNipDTO> searchJulgamentoJari(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		String periodicidade = searchCriterios.getAndRemoveCriterio("periodicidade").getValue();
		String aggregate = searchCriterios.getAndRemoveCriterio("aggregate_function").getValue();
		searchCriterios.addCriterios("A.tp_status", TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() + ", " + TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey() , ItemComparator.IN, Types.INTEGER);
		searchCriterios.addCriteriosEqualInteger("B.tp_status", TipoStatusEnum.DEFESA_PREVIA.getKey());
		searchCriterios.addCriteriosEqualInteger("B.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey());
		Search<RelatorioEstatisticasNipDTO> search = new SearchBuilder<RelatorioEstatisticasNipDTO>("mob_ait_movimento A")
				.fields(periodicidade + ", COUNT(*) AS qt_notificacao, COUNT(CASE WHEN A.tp_status = " + TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()
						+ " THEN A.cd_movimento END) as qt_deferimento, COUNT(CASE WHEN A.tp_status = " + TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey() + " THEN A.cd_movimento END) AS qt_indeferimento, "
						+ "(COUNT(CASE WHEN A.tp_status = " + TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() + " THEN '*' END)::float /"
						+ " COUNT(*)::float * 100) as pr_deferimento, (COUNT(CASE WHEN A.tp_status = " + TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey() + " THEN '*' END) "
						+ "::float / COUNT(*)::float * 100) AS pr_indeferimento, EXTRACT(days FROM AVG(A.dt_movimento - B.dt_movimento)) AS "
						+ "tempo_julgamento, SUM(CASE WHEN A.tp_status = " + TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() + " THEN "
						+ "C.vl_multa ELSE 0 END) as total_deferimentos ")
				.searchCriterios(searchCriterios)
				.addJoinTable("JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait) ")
				.addJoinTable("JOIN mob_ait C ON (A.cd_ait = C.cd_ait) ")
				.groupBy(aggregate)
				.orderBy(aggregate)
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}


	@Override
	public PagedResponse<RelatorioEstatisticasNipDTO> findPagamentoNip(SearchCriterios searchCriterios) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(false);
	        Search<RelatorioEstatisticasNipDTO> search = searchPagamentoNip(searchCriterios, customConnection);
	        customConnection.finishConnection();
	        return new PagedResponse<RelatorioEstatisticasNipDTO>(search.getList(RelatorioEstatisticasNipDTO.class), search.getRsm().getTotal());
	    } finally {
	        customConnection.closeConnection();
	    }
	}

	public Search<RelatorioEstatisticasNipDTO> searchPagamentoNip(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		String periodicidade = searchCriterios.getAndRemoveCriterio("periodicidade").getValue();
		String aggregate = searchCriterios.getAndRemoveCriterio("aggregate_function").getValue();
		searchCriterios.addCriterios("A.tp_status", TipoStatusEnum.NIP_ENVIADA.getKey() + ", " + TipoStatusEnum.MULTA_PAGA.getKey() , ItemComparator.IN, Types.INTEGER);
		Search<RelatorioEstatisticasNipDTO> search = new SearchBuilder<RelatorioEstatisticasNipDTO>("mob_ait_movimento A")
				.fields(periodicidade + ", COUNT(DISTINCT A.cd_ait) AS qt_vencida, "
					    + "COUNT(DISTINCT CASE WHEN A.tp_status = " + TipoStatusEnum.MULTA_PAGA.getKey() + " AND A.dt_movimento <= B.dt_vencimento THEN A.cd_ait END) AS qt_antes_vencimento, "
					    + "COUNT(DISTINCT CASE WHEN A.tp_status = " + TipoStatusEnum.MULTA_PAGA.getKey() + " AND A.dt_movimento > B.dt_vencimento THEN A.cd_ait END) AS qt_pagas_atraso, "
					    + "COUNT(DISTINCT CASE WHEN A.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + " AND C.cd_ait IS NULL THEN A.cd_ait END) AS qt_inadimplentes ")
				.searchCriterios(searchCriterios)
				.addJoinTable("LEFT JOIN mob_ait B ON (A.cd_ait = B.cd_ait) ")
				.addJoinTable("LEFT JOIN mob_ait_movimento C ON C.tp_status = 24 AND (C.cd_ait = A.cd_ait) ")
				.groupBy(aggregate)
				.orderBy(aggregate)
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
}
