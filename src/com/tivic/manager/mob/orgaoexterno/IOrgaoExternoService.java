package com.tivic.manager.mob.orgaoexterno;

import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IOrgaoExternoService {

	public OrgaoExterno insert(OrgaoExterno orgaoExterno) throws Exception;
	public OrgaoExterno insert(OrgaoExterno orgaoExterno, CustomConnection customConnection) throws Exception;
	public OrgaoExterno update(OrgaoExterno orgaoExterno) throws Exception;
	public OrgaoExterno update(OrgaoExterno orgaoExterno, CustomConnection customConnection) throws Exception;
	public OrgaoExterno get(int cdOrgaoExterno) throws Exception;
	public OrgaoExterno get(int cdOrgaoExterno, CustomConnection customConnection) throws Exception;
	public PagedResponse<OrgaoExterno> findPaged(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<OrgaoExterno> findPaged(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<OrgaoExterno> find(SearchCriterios searchCriterios) throws Exception;
	public List<OrgaoExterno> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
