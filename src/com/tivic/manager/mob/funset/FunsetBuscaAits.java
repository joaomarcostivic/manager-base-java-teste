package com.tivic.manager.mob.funset;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.util.Util;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class FunsetBuscaAits {

	public List<FunsetAitDTO> find(FunsetParametrosEntrada funsetParametrosEntrada) throws SQLException, Exception{
		return find(funsetParametrosEntrada, null);
	}
	
	public List<FunsetAitDTO> find(FunsetParametrosEntrada funsetParametrosEntrada, Integer tpStatus) throws SQLException, Exception{
		SearchCriterios searchCriterios = getCriteriosFunset(funsetParametrosEntrada);
		Search search = new SearchBuilder("mob_ait_pagamento A")
				.fields("A.*, B.nr_placa, B.nr_ait, B.sg_uf_veiculo, B.vl_multa, B.dt_infracao, B.nr_codigo_barras, " + 
						"	B.lg_detran_febraban, B.dt_vencimento, B.nr_renainf, B.nr_controle, B.tp_status AS tp_status_atual, B.tp_cancelamento, B.nr_renavan, " + 
						"   C.nr_cod_detran, C.vl_infracao, P.tp_status as st_devolucao")
				.addJoinTable("JOIN mob_ait B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN mob_infracao C ON (B.cd_infracao = C.cd_infracao) ")
				.addJoinTable("JOIN mob_ait_movimento P on (P.cd_ait = A.cd_ait and P.tp_status IN("+(tpStatus != null ? tpStatus.intValue() : AitMovimentoServices.DEVOLUCAO_PAGAMENTO+", "+AitMovimentoServices.MULTA_PAGA)+")) ")
				.searchCriterios(searchCriterios)
				.orderBy("A.cd_ait")
				.build();
				
		List<FunsetAitDTO> funsetAits = new FunsetAitDTOListBuilder(search).build();
		return funsetAits;
	}
	

	public SearchCriterios getCriteriosFunset(FunsetParametrosEntrada funsetParametrosEntrada) {
		SearchCriterios searchCriterios = new SearchCriterios();
		GregorianCalendar dtInicial = getDataInicial(funsetParametrosEntrada.getNrMes(), funsetParametrosEntrada.getNrAno());
		GregorianCalendar dtFinal = getDataFinal(funsetParametrosEntrada.getNrMes(), funsetParametrosEntrada.getNrAno());
		searchCriterios.addCriterios("B.dt_infracao", Util.convCalendarStringSql(dtInicial), ItemComparator.GREATER_EQUAL, Types.VARCHAR, dtInicial != null);
		searchCriterios.addCriterios("B.dt_infracao", Util.convCalendarStringSql(dtFinal), ItemComparator.MINOR_EQUAL, Types.VARCHAR, dtFinal != null);
		searchCriterios.addCriterios("B.dt_infracao", Util.convCalendarStringSql(dtInicial), ItemComparator.GREATER_EQUAL, Types.VARCHAR, funsetParametrosEntrada.getNrCodigoBancoArrecadador() != null);
		searchCriterios.addCriterios("A.vl_pago", "", ItemComparator.NOTISNULL, Types.VARCHAR);
		searchCriterios.addCriterios("lg_detran_febraban", "1", ItemComparator.EQUAL, Types.INTEGER);
		return searchCriterios;
	}
	

	private GregorianCalendar getDataInicial(String nrMes, String nrAno) {
		return new GregorianCalendar(Integer.parseInt(nrAno), Integer.parseInt(nrMes), 1);
	}

	private GregorianCalendar getDataFinal(String nrMes, String nrAno) {
		GregorianCalendar dtFinal = new GregorianCalendar(Integer.parseInt(nrAno), Integer.parseInt(nrMes), 1);
		dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return dtFinal;
	}

}
