package com.tivic.manager.mob.orgao;

import java.util.List;

import com.tivic.manager.mob.Orgao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface OrgaoRepository {

	public Orgao insert(Orgao orgao, CustomConnection customConnection) throws Exception;
	public Orgao update(Orgao orgao, CustomConnection customConnection) throws Exception;
	public Orgao get(int cdOrgao) throws Exception;
	public Orgao get(int cdOrgao, CustomConnection customConnection) throws Exception;
	public List<Orgao> find(SearchCriterios searchCriterios) throws Exception;
	public List<Orgao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

}
