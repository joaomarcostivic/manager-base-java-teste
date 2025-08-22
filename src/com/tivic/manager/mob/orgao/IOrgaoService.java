package com.tivic.manager.mob.orgao;

import com.tivic.manager.mob.Orgao;
import com.tivic.sol.connection.CustomConnection;

public interface IOrgaoService {
	public Orgao getByNome(String nmOrgao) throws Exception;
	public Orgao getById(String idOrgao) throws Exception;
	public Orgao getOrgaoUnico() throws Exception;
	public Orgao getByCdOrgao(int cdOrgao, CustomConnection customConnection) throws Exception;
}
