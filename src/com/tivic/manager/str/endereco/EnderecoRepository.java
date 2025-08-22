package com.tivic.manager.str.endereco;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface EnderecoRepository {
	void insert(Endereco endereco, CustomConnection customConnection) throws Exception;
	void update(Endereco endereco, CustomConnection customConnection) throws Exception;
	Endereco get(int cdEndereco, CustomConnection customConnection) throws Exception;
	List<Endereco> find(SearchCriterios searchCriterios) throws Exception;
	List<Endereco> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
