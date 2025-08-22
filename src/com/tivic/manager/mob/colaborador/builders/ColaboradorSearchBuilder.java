package com.tivic.manager.mob.colaborador.builders;

import com.tivic.sol.search.SearchCriterios;

public class ColaboradorSearchBuilder {
	private SearchCriterios searchCriterios;
	
	public ColaboradorSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public ColaboradorSearchBuilder setNmPessoa(String nmRelator) {
		searchCriterios.addCriteriosLikeAnyString("A.nm_pessoa", nmRelator, nmRelator != null);
		return this;
	}
	
	public ColaboradorSearchBuilder setCdVinculo(int cdVinculo) {
		searchCriterios.addCriteriosEqualInteger("D.cd_vinculo", cdVinculo, cdVinculo > -1);
		return this;
	}
	
	public ColaboradorSearchBuilder setStVinculo(int stVinculo) {
		searchCriterios.addCriteriosEqualInteger("C.st_vinculo", stVinculo, stVinculo > -1);
		return this;
	}
	
	public ColaboradorSearchBuilder setNrCpf(String cpfRelator) {
		searchCriterios.addCriteriosEqualString("B.nr_cpf", cpfRelator, cpfRelator != null);
		return this;
	}
	
	public ColaboradorSearchBuilder setDtVinculoInicial(String dtVinculoInicial) {
		searchCriterios.addCriteriosGreaterDate("C.dt_vinculo", dtVinculoInicial, dtVinculoInicial != null);
		return this;
	}
	
	public ColaboradorSearchBuilder setDtVinculoFinal(String dtVinculoFinal) {
		searchCriterios.addCriteriosMinorDate("C.dt_vinculo", dtVinculoFinal, dtVinculoFinal != null);
		return this;
	}
	
	public ColaboradorSearchBuilder setQtDelocamento(int limit, int page) {
		searchCriterios.setQtDeslocamento((limit*page)-limit);
		return this;
	}
	
	public ColaboradorSearchBuilder setQtLimit(int limit) {
		searchCriterios.setQtLimite(limit);
		return this;
	}
	
	public SearchCriterios build() {
		return this.searchCriterios;
	}

}
