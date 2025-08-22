package com.tivic.manager.fta.cor;

import java.util.List;

import com.tivic.manager.fta.Cor;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface CorRepository {
	public void insert(Cor cor, CustomConnection customConnection) throws Exception;
	public void update(Cor cor, CustomConnection customConnection) throws Exception;
	public Cor get(int cdCor) throws Exception;
	public Cor get(int cdCor, CustomConnection customConnection) throws Exception;
	public List<Cor> find(SearchCriterios searchCriterios) throws Exception;
	public List<Cor> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
