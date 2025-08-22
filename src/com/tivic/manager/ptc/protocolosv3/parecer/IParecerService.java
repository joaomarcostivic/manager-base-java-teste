package com.tivic.manager.ptc.protocolosv3.parecer;

import java.util.List;

import com.tivic.sol.search.SearchCriterios;

public interface IParecerService {
	public Parecer get(int cdParecer) throws Exception;
	public List<Parecer> find(SearchCriterios searchCriterios) throws Exception;
	public void insert(Parecer parecer) throws Exception;
	public void update(Parecer parecer) throws Exception;
	public void delete(int cdParecer) throws Exception;
}