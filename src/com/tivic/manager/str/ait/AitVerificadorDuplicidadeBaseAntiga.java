package com.tivic.manager.str.ait;

import java.sql.Connection;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.str.Ait;
import com.tivic.manager.util.Util;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitVerificadorDuplicidadeBaseAntiga implements IAitVerificadorDuplicidade {
	
	private List<Ait> aits;
	private final int MAXIMO_CARACTERES_NR_AIT = 10;

	@Override
	public IAitVerificadorDuplicidade findByNrAit(Ait ait, Connection customConnection) throws Exception {	
		SearchCriterios searchCriterios = new SearchCriterios();
		String idAit = ait.getIdAit() != null ? ait.getIdAit() : buildIdAit(ait);

		searchCriterios.addCriteriosEqualString("nr_ait", idAit);
		searchCriterios.setQtLimite(1);

		Search<Ait> search = new SearchBuilder<Ait>("ait")
							 .customConnection(null)
							 .fields("*")
							 .searchCriterios(searchCriterios)
							 .build();

		this.aits = search.getList(Ait.class);
		
		return this;
	}

	@Override
	public IAitVerificadorDuplicidade findByIdAit(com.tivic.manager.mob.Ait ait, Connection customConnection) throws Exception {
		throw new UnsupportedOperationException("Not implemented, yet.");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Ait get() throws Exception {
		return aits != null && aits.size() > 0 ? aits.get(0) : null;
	}

	public String buildIdAit(Ait ait) throws Exception {
		if(ait.getCdTalao() == 0) {
			return String.valueOf(ait.getNrAit());
		}
		
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cod_talao", ait.getCdTalao(), ait.getNrAit() > 0);
		searchCriterios.setQtLimite(1);

		Search<Talonario> search = new SearchBuilder<Talonario>("talonario")
							 .fields("*, cod_talao as cd_talao")
							 .searchCriterios(searchCriterios)
							 .build();
		
		if(search.getList(Talonario.class).size() == 0) {
			throw new BadRequestException("O AIT possui um código de talão, porém não foi localizado um talonário correspondente.");
		}
		
		Talonario talao = search.getList(Talonario.class).get(0);
		String sgTalao = talao.getSgTalao() != null ? talao.getSgTalao() : "";
		
		return talao.getSgTalao() != null ? 
			   sgTalao + Util.fillNum(ait.getNrAit(), MAXIMO_CARACTERES_NR_AIT - sgTalao.length()) : 
			   String.valueOf(ait.getNrAit());
	}

}
