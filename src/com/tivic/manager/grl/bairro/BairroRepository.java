package com.tivic.manager.grl.bairro;

import java.util.List;

import com.tivic.manager.grl.Bairro;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface BairroRepository {
	public void insert(Bairro bairro, CustomConnection customConnection) throws Exception;
	public void update(Bairro bairro, CustomConnection customConnection) throws Exception;
	public Bairro get(int cdBairro) throws Exception;
	public Bairro get(int cdBairro, CustomConnection customConnection) throws Exception;
	public List<Bairro> find(SearchCriterios searchCriterios) throws Exception;
	public List<Bairro> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<Bairro> findForApp(int cdCidade, CustomConnection customConnection) throws Exception;
}
