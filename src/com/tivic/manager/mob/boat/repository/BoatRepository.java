package com.tivic.manager.mob.boat.repository;

import java.util.List;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.boat.Boat;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface BoatRepository {
	public Boat insert(Boat boat, CustomConnection customConnection) throws Exception;
	public Boat update(Boat boat, CustomConnection customConnection) throws Exception;
	public Boat get(int cdBoat) throws Exception;
	public Boat get(int cdBoat, CustomConnection customConnection) throws Exception;
	public List<Boat> find(SearchCriterios searchCriterios) throws Exception;
	public List<Boat> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public int getUltimoNrBoat(SearchCriterios searchCriterios, Talonario talonario, CustomConnection customConnection) throws Exception;
}
