package com.tivic.manager.mob.talonario.factory.ultimodocumento;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.trrav.TrravRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class UltimoTrravStrategy implements UltimoNrDocumentoStrategy {
	private TrravRepository trravRepository;
	private SearchCriterios searchCriterios;
	private Talonario talonario;
	
	public UltimoTrravStrategy(SearchCriterios searchCriterios, Talonario talonario) throws Exception  {
		this.trravRepository = (TrravRepository) BeansFactory.get(TrravRepository.class);
		this.searchCriterios = searchCriterios;
		this.talonario = talonario;
	}
	
    @Override
    public int getUltimoNrDocumento(CustomConnection customConnection) throws Exception {
        return trravRepository.getUltimoNrTrrav(searchCriterios, talonario, customConnection);
    }
}
