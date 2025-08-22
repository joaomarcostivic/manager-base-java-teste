package com.tivic.manager.mob.pessoa;

import java.util.List;

import com.tivic.manager.grl.Pessoa;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface PessoaRepository {
	Pessoa insert(Pessoa pessoa, CustomConnection customConnection) throws Exception;
	Pessoa update(Pessoa pessoa, CustomConnection customConnection) throws Exception;
	Pessoa delete(Pessoa pessoa, CustomConnection customConnection) throws Exception;
	Pessoa get(int cdPessoa) throws Exception;
	Pessoa get(int cdPessoa, CustomConnection customConnection) throws Exception;
	List<Pessoa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
