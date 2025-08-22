package com.tivic.manager.mob.ait.aitpessoa;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface AitPessoaRepository {
	
	void insert(AitPessoa aitPessoa, CustomConnection customConnection) throws Exception;
	void update(AitPessoa aitPessoa, CustomConnection customConnection) throws Exception;
	AitPessoa get(int cdAitPessoa, CustomConnection customConnection) throws Exception;
	List<AitPessoa> find(SearchCriterios searchCriterios) throws Exception;
	List<AitPessoa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

}
