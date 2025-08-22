package com.tivic.manager.mob.rrd;

import java.util.List;

import com.tivic.manager.mob.Rrd;
import com.tivic.manager.mob.Talonario;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface RrdRepository {
	public Rrd insert(Rrd rrd, CustomConnection customConnection) throws Exception;
	public Rrd update(Rrd rrd, CustomConnection customConnection) throws Exception;
	public Rrd get(int cdRrd, CustomConnection customConnection) throws Exception;
	public List<Rrd> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public int getUltimoNrRrd(SearchCriterios searchCriterios, Talonario talonario, CustomConnection customConnection) throws Exception;
}
