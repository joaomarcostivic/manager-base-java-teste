package com.tivic.manager.mob.convenio;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ConvenioRepository {
	void insert(Convenio convenio, CustomConnection customConnection) throws Exception; 
	void update(Convenio convenio, CustomConnection customConnection) throws Exception; 
	Convenio get(int cdConvenio, CustomConnection customConnection) throws Exception; 
	List<Convenio> find(SearchCriterios searchCriterios) throws Exception; 
	List<Convenio> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception; 
}
