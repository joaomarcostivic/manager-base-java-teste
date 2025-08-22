package com.tivic.manager.grl.pessoavinculohistorico.builders;

import com.tivic.sol.search.SearchCriterios;

public class PessoaHistoricoSearchBuilder {
	private SearchCriterios criterios;
	
	public PessoaHistoricoSearchBuilder() {
		criterios = new SearchCriterios();
	}
	
	public PessoaHistoricoSearchBuilder setCdPessoa(int cdPessoa) {
		criterios.addCriteriosEqualInteger("cd_pessoa", cdPessoa);
		return this;
	}
	
	public PessoaHistoricoSearchBuilder setQtDeslocamento(int limit, int page) {
		criterios.setQtDeslocamento((limit*page)-limit);
		return this;		
	}
	
	public PessoaHistoricoSearchBuilder setQtLimite(int limit) {
		criterios.setQtLimite(limit);
		return this;
	}
	
	public SearchCriterios build() {
		return this.criterios;
	}

}
