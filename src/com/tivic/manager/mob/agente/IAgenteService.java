package com.tivic.manager.mob.agente;

import java.util.List;

import com.tivic.manager.mob.Agente;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IAgenteService {
	public Agente getByMatricula(String nrMatricula) throws Exception;
	public Agente getByMatricula(String nrMatricula, CustomConnection customConnection) throws Exception;
	public List<Agente> get(int cdAgente) throws Exception;
	public List<Agente> get(int cdAgente, CustomConnection customConnection) throws Exception;
	public List<Agente> find(SearchCriterios searchCriterios) throws Exception;
	public List<Agente> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public void delete(int cdAgente) throws Exception;
	public void delete(int cdAgente, CustomConnection customConnection) throws Exception;
}
