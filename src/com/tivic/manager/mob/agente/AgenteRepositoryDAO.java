package com.tivic.manager.mob.agente;

import java.util.List;

import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.AgenteDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AgenteRepositoryDAO implements AgenteRepository {

	@Override
	public void insert(Agente agente, CustomConnection customConnection) throws Exception {
		int cdAgente = AgenteDAO.insert(agente, customConnection.getConnection());
		if (cdAgente <= 0)
			throw new Exception("Erro ao inserir Agente.");
		agente.setCdAgente(cdAgente);
	}

	@Override
	public void update(Agente agente, CustomConnection customConnection) throws Exception {
		AgenteDAO.update(agente, customConnection.getConnection());
	}
	
	@Override
	public Agente get(int cdAgente) throws Exception {
		return get(cdAgente, new CustomConnection());
	}

	@Override
	public Agente get(int cdAgente, CustomConnection customConnection) throws Exception {
		return AgenteDAO.get(cdAgente, customConnection.getConnection());
	}
	
	@Override
	public Agente getByCdUsuario(int cdUsuario) throws Exception {
	    return getByCdUsuario(cdUsuario, new CustomConnection());
	}

	@Override
	public Agente getByCdUsuario(int cdUsuario, CustomConnection customConnection) throws Exception {
	    return AgenteDAO.getByCdUsuario(cdUsuario, customConnection.getConnection());
	}

	@Override
	public List<Agente> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Agente> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Agente> search = new SearchBuilder<Agente>("mob_agente")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(Agente.class);
	}

	@Override
	public void delete(int cdAgente, CustomConnection customConnection) throws Exception {
		int agente = AgenteDAO.delete(cdAgente, customConnection.getConnection());
		if(agente <= 0) {
			throw new Exception("Erro ao deletar agente");
		}
		
	}
}
