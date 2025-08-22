package com.tivic.manager.mob.rrd;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.Rrd;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IRrdService {
	public Rrd insert(Rrd rrd) throws BadRequestException, Exception;
	public Rrd insert(Rrd rrd, CustomConnection customConnection) throws BadRequestException, Exception;
	public Rrd update(Rrd rrd) throws Exception;
	public Rrd update(Rrd rrd, CustomConnection customConnection) throws Exception;
	public Rrd get(int cdRrd) throws Exception;
	public Rrd get(int cdRrd, CustomConnection customConnection) throws Exception;
	public List<Rrd> find(SearchCriterios searchCriterios) throws Exception;
	public List<Rrd> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
