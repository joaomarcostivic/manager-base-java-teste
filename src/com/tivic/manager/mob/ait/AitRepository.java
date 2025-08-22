package com.tivic.manager.mob.ait;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Talonario;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface AitRepository {
	public Ait insert(Ait ait, CustomConnection customConnection) throws Exception;
	public Ait update(Ait ait, CustomConnection customConnection) throws Exception;
	public Ait get(int cdAit) throws Exception;
	public Ait get(int cdAit, CustomConnection customConnection) throws Exception;
	public List<Ait> find(SearchCriterios searchCriterios) throws Exception;
	public List<Ait> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<Ait> findByEvento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public boolean hasAit(String idAit, CustomConnection customConnection) throws Exception;
	public int getUltimoNrAitByTalao(Talonario talonario, CustomConnection customConnection) throws Exception;
}


