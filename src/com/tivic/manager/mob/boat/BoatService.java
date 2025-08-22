package com.tivic.manager.mob.boat;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.boat.repository.BoatRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class BoatService implements IBoatService {
	BoatRepository boatRepository;  

	public BoatService() throws Exception {
		boatRepository = (BoatRepository) BeansFactory.get(BoatRepository.class);
	}
	
	@Override
	public Boat insert(Boat boat) throws BadRequestException, Exception {
		return this.boatRepository.insert(boat, null);
	}

	@Override
	public Boat insert(Boat boat, CustomConnection customConnection)
			throws BadRequestException, Exception {
		return this.boatRepository.insert(boat, customConnection);
	}

	@Override
	public Boat update(Boat boat) throws Exception {
		return this.boatRepository.update(boat, new CustomConnection());
	}

	@Override
	public Boat update(Boat boat, CustomConnection customConnection) throws Exception {
		return this.boatRepository.update(boat, customConnection);
	}

	@Override
	public Boat get(int cdBoat) throws Exception {
		return this.boatRepository.get(cdBoat, new CustomConnection());
	}

	@Override
	public Boat get(int cdBoat, com.tivic.sol.connection.CustomConnection customConnection) throws Exception {
		return this.boatRepository.get(cdBoat, customConnection);
	}
	
	@Override
	public List<Boat> find(SearchCriterios searchCriterios) throws Exception {
		return this.boatRepository.find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Boat> find(SearchCriterios searchCriterios, CustomConnection customConnection)
			throws Exception {
		return this.boatRepository.find(searchCriterios, customConnection);
	}

}
