package com.tivic.manager.mob.guiapagamento;

import com.tivic.sol.search.SearchCriterios;

public class GuiaPagamentoSearchBuilder {

	private SearchCriterios searchCriterios;
	
	GuiaPagamentoSearchBuilder() {
		this.searchCriterios = new SearchCriterios(); 
	}
	
	public GuiaPagamentoSearchBuilder setIdAit(String idAit) {
		this.searchCriterios.addCriteriosEqualString("A.id_ait", idAit, idAit != null);
		return this;
	}
	
	public GuiaPagamentoSearchBuilder setNrCpfCnpjProprietario(String nrCpfCnpjProprietario) {
		this.searchCriterios.addCriteriosEqualString("A.nr_cpf_cnpj_proprietario", nrCpfCnpjProprietario, nrCpfCnpjProprietario != null);
		return this;
	}
	
	public GuiaPagamentoSearchBuilder setNrPlaca(String nrPlaca) {
		this.searchCriterios.addCriteriosEqualString("A.nr_placa", nrPlaca, nrPlaca != null);
		return this;
	}
	
	public GuiaPagamentoSearchBuilder setLimit(int limit, int page) {
		this.searchCriterios.setQtLimite(limit);
		this.searchCriterios.setQtDeslocamento(limit * page - limit);
    	return this;
    }
	
	public SearchCriterios build() {
		return this.searchCriterios;
	}
}
