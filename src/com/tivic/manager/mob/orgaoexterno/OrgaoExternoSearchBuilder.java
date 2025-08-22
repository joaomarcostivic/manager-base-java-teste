package com.tivic.manager.mob.orgaoexterno;

import com.tivic.sol.search.SearchCriterios;

public class OrgaoExternoSearchBuilder {
	
	private SearchCriterios searchCriterios;
	
	public OrgaoExternoSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public OrgaoExternoSearchBuilder setCdOrgaoExterno(int cdOrgaoExterno) {
		searchCriterios.addCriteriosEqualInteger("cd_orgao_externo", cdOrgaoExterno, cdOrgaoExterno > 0);
		return this;
	}
	
	public OrgaoExternoSearchBuilder setNmOrgaoExterno(String nmOrgaoExterno) {
		searchCriterios.addCriteriosEqualString("nm_orgao_externo", nmOrgaoExterno, nmOrgaoExterno != null);
		return this;
	}
	
	public OrgaoExternoSearchBuilder setSgOrgaoExterno(String sgOrgaoExterno) {
		searchCriterios.addCriteriosEqualString("sg_orgao_externo", sgOrgaoExterno, sgOrgaoExterno != null);
		return this;
	}
	
	public OrgaoExternoSearchBuilder setCdTipoLogradouro(int cdTipoLogradouro) {
		searchCriterios.addCriteriosEqualInteger("cd_tipo_logradouro", cdTipoLogradouro, cdTipoLogradouro > 0);
		return this;
	}
	
	public OrgaoExternoSearchBuilder setNmLogradouro(String nmLogradouro) {
		searchCriterios.addCriteriosEqualString("nm_logradouro", nmLogradouro, nmLogradouro != null);
		return this;
	}
	
	public OrgaoExternoSearchBuilder setCdCidade(int cdCidade) {
		searchCriterios.addCriteriosEqualInteger("cd_cidade", cdCidade, cdCidade > 0);
		return this;
	}
	
	public OrgaoExternoSearchBuilder setCdBairro(int cdBairro) {
		searchCriterios.addCriteriosEqualInteger("cd_bairro", cdBairro, cdBairro > 0);
		return this;
	}
	
	public OrgaoExternoSearchBuilder setNrCep(String nrCep) {
		searchCriterios.addCriteriosEqualString("nr_cep", nrCep, nrCep != null);
		return this;
	}

	public OrgaoExternoSearchBuilder setLimit(int limit, int page) {
		searchCriterios.setQtLimite(limit);
		searchCriterios.setQtDeslocamento(limit * page - limit);
    	return this;
    }
	
	public SearchCriterios build() {
        return searchCriterios;
    }
}