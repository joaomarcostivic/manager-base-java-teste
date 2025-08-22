package com.tivic.manager.ptc.portal.builders;

import com.tivic.sol.search.SearchCriterios;

public class AitSearchBuilder {
	private SearchCriterios criterios;

	public AitSearchBuilder() {
		this.criterios = new SearchCriterios();
	}

	public AitSearchBuilder setIdAit(String idAit) {
		this.criterios.addCriteriosEqualString("id_ait", idAit, idAit != null && !idAit.trim().equals(""));
		return this;
	}

	public AitSearchBuilder setNrPlaca(String nrPlaca) {
		this.criterios.addCriteriosEqualString("nr_placa", nrPlaca, nrPlaca != null && !nrPlaca.trim().equals(""));
		return this;
	}

	public AitSearchBuilder setNrRenavan(String nrRenavan) {
		this.criterios.addCriteriosEqualString("nr_renavan", nrRenavan, nrRenavan != null && !nrRenavan.trim().equals(""));
		return this;
	}

	public SearchCriterios build() {
		return this.criterios;
	}
}
