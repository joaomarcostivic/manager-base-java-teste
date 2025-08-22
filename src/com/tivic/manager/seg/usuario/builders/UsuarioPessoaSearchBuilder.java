package com.tivic.manager.seg.usuario.builders;

import com.tivic.sol.search.SearchCriterios;

public class UsuarioPessoaSearchBuilder {
	
	private SearchCriterios criterios;
	
	public UsuarioPessoaSearchBuilder() {
		criterios = new SearchCriterios();
	}
	
	public UsuarioPessoaSearchBuilder setNmLogin(String nmLogin) {
		criterios.addCriteriosEqualString("nm_login", nmLogin, nmLogin != null);
		return this;
	}
	
	public UsuarioPessoaSearchBuilder setNmPessoa(String nmPessoa) {
		criterios.addCriteriosEqualString("nm_pessoa", nmPessoa);
		return this;
	}

	public UsuarioPessoaSearchBuilder setNmEmail(String nmEmail) {
		criterios.addCriteriosEqualString("nm_email", nmEmail);
		return this;
	
	}
	public SearchCriterios build() {
		return criterios;
	}
}