package com.tivic.manager.mob.talonario;

import java.util.List;

import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.Talonario;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface TalonarioRepository {
	public void insert(Talonario talonario, CustomConnection customConnection) throws Exception;
	public void update(Talonario talonario, CustomConnection customConnection) throws Exception;
	public Talonario get(int cdTalao, CustomConnection customConnection) throws Exception;
	public List<Talonario> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<Talonario> getByAgente(Agente agente, SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<Talonario> getEletronicoByAgente(int cdAgente, CustomConnection customConnection) throws Exception;
}
