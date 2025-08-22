package com.tivic.manager.mob.veiculosmultavencida.builders;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

public class VeiculosMultaVencidaSearchBuilder {
	private SearchCriterios searchCriterios;

	public VeiculosMultaVencidaSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public VeiculosMultaVencidaSearchBuilder setNmProprietario(String nmProprietario) {
		searchCriterios.addCriteriosLikeAnyString("A.nm_proprietario", nmProprietario, nmProprietario != null);
		return this;
	}

	public VeiculosMultaVencidaSearchBuilder setNrPlaca(String nrPlaca) {
		searchCriterios.addCriteriosLikeAnyString("A.nr_placa", nrPlaca, nrPlaca != null);
		return this;
	}

	public VeiculosMultaVencidaSearchBuilder setQtDiasAtraso(int qtDiasAtraso) {
		if (qtDiasAtraso > 0) {
			GregorianCalendar dtHoje = DateUtil.getDataAtual();
			dtHoje.add(Calendar.DAY_OF_MONTH, qtDiasAtraso * (-1));
			searchCriterios.addCriteriosGreaterDate("A.dt_vencimento", DateUtil.formatDate(new Timestamp(dtHoje.getTimeInMillis()), "yyyy-MM-dd"));
		}
		return this;
	}
	
	public SearchCriterios build() {
        return searchCriterios;
    }
}
