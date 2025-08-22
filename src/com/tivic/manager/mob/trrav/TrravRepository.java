package com.tivic.manager.mob.trrav;

import java.util.List;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.Trrav;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface TrravRepository {
	public Trrav insert(Trrav trrav, CustomConnection customConnection) throws Exception;
	public Trrav update(Trrav trrav, CustomConnection customConnection) throws Exception;
	public Trrav get(int cdTrrav, CustomConnection customConnection) throws Exception;
	public List<Trrav> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public int getUltimoNrTrrav(SearchCriterios searchCriterios, Talonario talonario, CustomConnection customConnection) throws Exception;
}
