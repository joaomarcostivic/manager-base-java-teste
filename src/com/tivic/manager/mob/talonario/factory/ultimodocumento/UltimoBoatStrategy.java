package com.tivic.manager.mob.talonario.factory.ultimodocumento;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.boat.repository.BoatRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class UltimoBoatStrategy implements UltimoNrDocumentoStrategy {
	private BoatRepository boatRepository;
	private SearchCriterios searchCriterios;
	private Talonario talonario;
	
	public UltimoBoatStrategy(SearchCriterios searchCriterios, Talonario talonario) throws Exception  {
		this.boatRepository = (BoatRepository) BeansFactory.get(BoatRepository.class);
		this.searchCriterios = searchCriterios;
		this.talonario = talonario;
	}
	
    @Override
    public int getUltimoNrDocumento(CustomConnection customConnection) throws Exception {
        return boatRepository.getUltimoNrBoat(searchCriterios, talonario, customConnection);
    }
}
