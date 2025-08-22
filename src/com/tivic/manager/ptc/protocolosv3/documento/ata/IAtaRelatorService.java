package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IAtaRelatorService {
	void insertRelatores(int cdAta, List<Integer> listCdPessoa, CustomConnection customConnection) throws Exception;
	int insert(AtaRelator ataRelator, CustomConnection customConnection) throws Exception;
	List<AtaRelator> find(SearchCriterios searchCriterios) throws Exception;
	List<AtaRelator> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	List<AtaRelatorDTO> getAtaRelator(int cdAta) throws Exception;
	List<AtaRelatorDTO> getAtaRelator(int cdAta, CustomConnection customConnection) throws Exception;
}
