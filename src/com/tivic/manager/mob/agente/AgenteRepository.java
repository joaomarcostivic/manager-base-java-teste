package com.tivic.manager.mob.agente;

import java.util.List;

import com.tivic.manager.mob.Agente;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface AgenteRepository {
	public void insert(Agente agente, CustomConnection customConnection) throws Exception;
	public void update(Agente agente, CustomConnection customConnection) throws Exception;
	public Agente get(int cdAgente) throws Exception;
	public Agente get(int cdAgente, CustomConnection customConnection) throws Exception;
	public Agente getByCdUsuario(int cdUsuario) throws Exception;
	public Agente getByCdUsuario(int cdUsuario, CustomConnection customConnection) throws Exception;
	public List<Agente> find(SearchCriterios searchCriterios) throws Exception;
	public List<Agente> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public void delete(int cdAgente, CustomConnection customConnection) throws Exception;
}
