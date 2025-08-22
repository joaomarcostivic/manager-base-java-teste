package com.tivic.manager.mob.orgaoexterno;

import com.tivic.sol.connection.CustomConnection;

public class OrgaoExternoRepositoryDAO implements OrgaoExternoRepository{

	@Override
	public OrgaoExterno insert(OrgaoExterno orgaoExterno, CustomConnection customConnection) throws Exception {
		int cdOrgaoExterno = OrgaoExternoDAO.insert(orgaoExterno, customConnection.getConnection());
		if(cdOrgaoExterno <= 0)
			throw new Exception("Erro ao inserir orgão externo");
		return orgaoExterno;
	}

	@Override
	public OrgaoExterno update(OrgaoExterno orgaoExterno, CustomConnection customConnection) throws Exception {
		int cdOrgaoExterno = OrgaoExternoDAO.update(orgaoExterno, customConnection.getConnection());
		if (cdOrgaoExterno <= 0)
			throw new Exception("Erro ao atualizar orgão externo.");
		return orgaoExterno;
	}

	@Override
	public OrgaoExterno get(int orgaoExterno) throws Exception {
		return get(orgaoExterno, new CustomConnection());
	}

	@Override
	public OrgaoExterno get(int orgaoExterno, CustomConnection customConnection) throws Exception {
		return OrgaoExternoDAO.get(orgaoExterno, customConnection.getConnection());
	}

}
