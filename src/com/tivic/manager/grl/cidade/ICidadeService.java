package com.tivic.manager.grl.cidade;

import java.util.List;

import com.tivic.manager.grl.Cidade;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ICidadeService {
	public Cidade getByNome(String nmCidade) throws Exception;
	public Cidade getById(String idCidade) throws Exception;
	public List<Cidade> find(SearchCriterios searchCriterios) throws Exception;
	public List<Cidade> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Cidade get (int cdCidade) throws Exception;
	public Cidade get (int cdCidade, CustomConnection customConnection) throws Exception;
}
