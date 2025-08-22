package com.tivic.manager.mob.rrd;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.Rrd;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class RrdService implements IRrdService {
	RrdRepository rrdRepository;  

	public RrdService() throws Exception {
		rrdRepository = (RrdRepository) BeansFactory.get(RrdRepository.class);
	}
	
	@Override
	public Rrd insert(Rrd rrd) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(rrd, customConnection);
			customConnection.finishConnection();
			return rrd;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Rrd insert(Rrd rrd, CustomConnection customConnection) throws BadRequestException, Exception {
		this.rrdRepository.insert(rrd, customConnection);
		return rrd;
	}

	@Override
	public Rrd update(Rrd rrd) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(rrd, customConnection);
			customConnection.finishConnection();
			return rrd;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Rrd update(Rrd rrd, CustomConnection customConnection) throws Exception {
		this.rrdRepository.update(rrd, customConnection);
		return rrd;
	}

	@Override
	public Rrd get(int cdRrd) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Rrd rrd = get(cdRrd, customConnection);
			customConnection.finishConnection();
			return rrd;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Rrd get(int cdRrd, CustomConnection customConnection) throws Exception {
		Rrd rrd = this.rrdRepository.get(cdRrd, customConnection);
		
		if(rrd == null)
			throw new NoContentException("Nenhum RRD encontrado");
		
		return rrd;
	}
	
	@Override
	public List<Rrd> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Rrd> rrds = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return rrds;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Rrd> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.rrdRepository.find(searchCriterios, customConnection);
	}

}
