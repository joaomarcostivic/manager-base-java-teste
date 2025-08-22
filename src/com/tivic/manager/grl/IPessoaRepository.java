package com.tivic.manager.grl;

import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IPessoaRepository {
	public Pessoa insert(Pessoa pessoa, CustomConnection customConnection) throws Exception;
	public Pessoa update(Pessoa pessoa, CustomConnection customConnection) throws Exception;
	public Pessoa delete(Pessoa pessoa, CustomConnection customConnection) throws Exception;
	public Pessoa get(int cdPessoa) throws Exception;
	public Pessoa get(int cdPessoa, CustomConnection customConnection) throws Exception;
	public List<Pessoa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}