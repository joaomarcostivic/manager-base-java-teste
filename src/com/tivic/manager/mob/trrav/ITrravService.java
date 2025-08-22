package com.tivic.manager.mob.trrav;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.Trrav;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ITrravService {
	public Trrav insert(Trrav trrav) throws BadRequestException, Exception;
	public Trrav insert(Trrav trrav, CustomConnection customConnection) throws BadRequestException, Exception;
	public Trrav update(Trrav trrav) throws Exception;
	public Trrav update(Trrav trrav, CustomConnection customConnection) throws Exception;
	public Trrav get(int cdTrrav) throws Exception;
	public Trrav get(int cdTrrav, CustomConnection customConnection) throws Exception;
	public List<Trrav> find(SearchCriterios searchCriterios) throws Exception;
	public List<Trrav> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
