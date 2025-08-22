package com.tivic.manager.mob.aitevento;

import java.util.List;

import com.tivic.manager.mob.AitEvento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IAitEventoService {
	public AitEvento insert(AitEvento ait) throws Exception;
	public AitEvento insert(AitEvento ait, CustomConnection customConnection) throws Exception;
	public AitEvento update(AitEvento ait) throws Exception;
	public AitEvento update(AitEvento ait, CustomConnection customConnection) throws Exception;
	public AitEvento get(int cdAit, int cdEvento) throws Exception;
	public AitEvento get(int cdAit, int cdEvento, CustomConnection customConnection) throws Exception;
	public List<AitEvento> find(SearchCriterios searchCriterios) throws Exception;
	public List<AitEvento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
