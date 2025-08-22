package com.tivic.manager.str.ait;

import java.sql.Connection;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.util.Util;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitVerificadorDuplicidadeBaseNova implements IAitVerificadorDuplicidade {
	
	private List<Ait> aits;

	public IAitVerificadorDuplicidade findByIdAit(Ait ait, Connection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_ait", ait.getIdAit(), validarIdAit(ait));
		searchCriterios.setQtLimite(1);

		Search<Ait> search = new SearchBuilder<Ait>("mob_ait")
							 .fields("*")
							 .searchCriterios(searchCriterios)
							 .build();

		this.aits = search.getList(Ait.class);
		
		return this;
	}

	@Override
	public IAitVerificadorDuplicidade findByNrAit(com.tivic.manager.str.Ait ait, Connection customConnection) throws Exception {
		throw new UnsupportedOperationException("Not implemented, yet.");
	}

	@Override
	@SuppressWarnings("unchecked")
	public Ait get() throws Exception {
		return aits != null && aits.size() > 0 ? aits.get(0) : null;
	}
	
	private boolean validarIdAit(Ait ait) {
		return ait.getIdAit() != null && !ait.getIdAit().trim().equals("");
	}

}
