package com.tivic.manager.mob.boat.repository;

import java.util.List;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.boat.Boat;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;


public class BoatRepositoryDAO implements BoatRepository {
	
	@Override
	public Boat insert(Boat Boat, CustomConnection customConnection) throws Exception {
		int cdBoat = BoatDAO.insert(Boat, customConnection.getConnection());
		if(cdBoat <= 0)
			throw new Exception("Erro ao inserir Boat");
		Boat.setCdBoat(cdBoat);
		return Boat;
	}

	@Override
	public Boat update(Boat Boat, CustomConnection customConnection) throws Exception {
		BoatDAO.update(Boat, customConnection.getConnection());
		return Boat;
	}
	
	@Override
	public Boat get(int cdBoat) throws Exception {
		return BoatDAO.get(cdBoat, new CustomConnection().getConnection());
	}

	@Override
	public Boat get(int cdBoat, CustomConnection customConnection) throws Exception {
		return BoatDAO.get(cdBoat, customConnection.getConnection());
	}
	
	@Override
	public List<Boat> find(SearchCriterios criterios) throws Exception {
		return find(criterios, new CustomConnection());
	}

	@Override
	public List<Boat> find(SearchCriterios criterios, CustomConnection customConnection) throws Exception {
		Search<Boat> search = new SearchBuilder<Boat>("mob_boat").searchCriterios(criterios).build();
		return search.getList(Boat.class);
	}
	
	@Override
	public int getUltimoNrBoat(SearchCriterios searchCriterios, Talonario talonario, CustomConnection customConnection) throws Exception {
		Search<Boat> search = new SearchBuilder<Boat>("mob_boat")
				.fields("nr_boat")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy("nr_boat DESC")
				.build();
		List<Boat> boats = search.getList(Boat.class);
		
		if(boats.isEmpty()) {
			return talonario.getNrInicial() > 0 ? talonario.getNrInicial() - 1 : 0;
		}
		
		return boats.get(0).getNrBoat();
	}

}
