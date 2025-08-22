package com.tivic.manager.mob.talonario.validatons;

import java.util.List;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class FaixaNumeroValidator implements Validator<Talonario> {
	@Override
	public void validate(Talonario talonario, CustomConnection customConnection) throws Exception {
		List<Talonario> lista = buscaTalonarios(talonario, customConnection);
		if (!lista.isEmpty()) {
			throw new Exception("Já existe esse tipo de talonário com essa numeração.");
		}
	}
	
	private List<Talonario> buscaTalonarios(Talonario talonario, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("tp_talao", talonario.getTpTalao());
		searchCriterios.addCriteriosEqualString("sg_talao", talonario.getSgTalao());
		try {
			customConnection.initConnection(false);
			Search<Talonario> search = new SearchBuilder<Talonario>("mob_talonario A")
					.fields("A.tp_talao, A.sg_talao, A.nr_inicial, A.nr_final")
					.additionalCriterias("nr_inicial BETWEEN " + talonario.getNrInicial() + " AND " + talonario.getNrFinal())
					.additionalCriterias("nr_final BETWEEN " + talonario.getNrInicial() + " AND " + talonario.getNrFinal())
					.searchCriterios(searchCriterios)
					.build();
			customConnection.finishConnection();
			return search.getList(Talonario.class);
		} finally {
			customConnection.closeConnection();
		}
	}
}
