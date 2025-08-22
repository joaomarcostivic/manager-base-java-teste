package com.tivic.manager.mob.agente;

import java.util.List;

import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.agente.validators.DeleteAgenteValidators;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AgenteService implements IAgenteService {

	private AgenteRepository agenteRepository;
	
	public AgenteService() throws Exception {
		this.agenteRepository = (AgenteRepository) BeansFactory.get(AgenteRepository.class);
	}
	
	@Override
	public Agente getByMatricula(String nrMatricula) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Agente agente = getByMatricula(nrMatricula, customConnection);
			customConnection.finishConnection();
			return agente;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Agente getByMatricula(String nrMatricula, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nr_matricula", nrMatricula, true);
		List<Agente> agentes = this.agenteRepository.find(searchCriterios, customConnection);
		if(agentes.isEmpty())
			throw new Exception("Nenhum agente encontrado");
		return agentes.get(0);
	}

	@Override
	public List<Agente> get(int cdAgente) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Agente> agentes = get(cdAgente, customConnection);
			customConnection.finishConnection();
			return agentes;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Agente> get(int cdAgente, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios(); 
		searchCriterios.addCriteriosEqualInteger("cd_agente", cdAgente);
		Search<Agente> search = new SearchBuilder<Agente>("mob_agente")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Agente.class);
	}

	@Override
	public List<Agente> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Agente> agentes = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return agentes;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Agente> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Agente> search = new SearchBuilder<Agente>("mob_agente")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy("nm_agente ASC")
				.build();
				
		return search.getList(Agente.class);
	}
	
	@Override
	public void delete(int cdAgente) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new DeleteAgenteValidators(cdAgente).validate(customConnection);
			delete(cdAgente, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void delete(int cdAgente, CustomConnection customConnection) throws Exception {
		agenteRepository.delete(cdAgente, customConnection);
	}
}
