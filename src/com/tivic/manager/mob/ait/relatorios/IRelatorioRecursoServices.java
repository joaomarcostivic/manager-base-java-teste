package com.tivic.manager.mob.ait.relatorios;

import com.tivic.sol.report.Report;
import com.tivic.sol.search.SearchCriterios;

public interface IRelatorioRecursoServices {
	public Report reportRecursoJari(SearchCriterios searchCriterios) throws Exception;
}
