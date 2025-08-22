package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IAtaRepository {
	int insert(Ata ata, CustomConnection customConnection) throws Exception;
	int update(Ata ata, CustomConnection customConnection) throws Exception;
	Ata get(int cdAta) throws Exception;
	Ata get(int cdAta, CustomConnection customConnection) throws Exception;
	List<Ata> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	void delete(int cdAta, CustomConnection customConnection) throws Exception;
}
