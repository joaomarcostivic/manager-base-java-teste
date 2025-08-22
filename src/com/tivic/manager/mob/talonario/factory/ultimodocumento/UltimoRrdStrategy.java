package com.tivic.manager.mob.talonario.factory.ultimodocumento;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.rrd.RrdRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class UltimoRrdStrategy implements UltimoNrDocumentoStrategy {
	private RrdRepository rrdRepository;
	private SearchCriterios searchCriterios;
	private Talonario talonario;
	
	public UltimoRrdStrategy(SearchCriterios searchCriterios, Talonario talonario) throws Exception  {
		this.rrdRepository = (RrdRepository) BeansFactory.get(RrdRepository.class);
		this.searchCriterios = searchCriterios;
		this.talonario = talonario;
	}
	
    @Override
    public int getUltimoNrDocumento(CustomConnection customConnection) throws Exception {
        return rrdRepository.getUltimoNrRrd(searchCriterios, talonario, customConnection);
    }
}
