package com.tivic.manager.mob.orgaoexterno;

import com.tivic.sol.connection.CustomConnection;

public interface OrgaoExternoRepository {

	public OrgaoExterno insert(OrgaoExterno orgaoExterno, CustomConnection customConnection) throws Exception;
	public OrgaoExterno update(OrgaoExterno orgaoExterno, CustomConnection customConnection) throws Exception;
	public OrgaoExterno get(int cdOrgaoExterno) throws Exception;
	public OrgaoExterno get(int cdOrgaoExterno, CustomConnection customConnection) throws Exception;
	
}
