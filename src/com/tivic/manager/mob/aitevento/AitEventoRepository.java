package com.tivic.manager.mob.aitevento;

import java.util.List;

import com.tivic.manager.mob.AitEvento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface AitEventoRepository {
	public void insert(AitEvento aitEvento, CustomConnection customConnection) throws Exception;
	public void update(AitEvento Evento, CustomConnection customConnection) throws Exception;
	public AitEvento get(int cdAit, int cdEvento, CustomConnection customConnection) throws Exception;
	public List<AitEvento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
