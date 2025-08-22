package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IAtaRelatorRepository {
	int insert(AtaRelator ataRelator, CustomConnection customConnection) throws Exception;
	int update(AtaRelator ataRelator, CustomConnection customConnection) throws Exception;
	AtaRelator get(int cdAta, int cdPessoa, CustomConnection customConnection) throws Exception;
	List<AtaRelator> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	void delete(int cdAta, int cdPessoa, CustomConnection customConnection) throws Exception;
}
