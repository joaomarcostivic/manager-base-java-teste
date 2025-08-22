package com.tivic.manager.mob.agente;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Agente;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AgenteRepositoryTest implements AgenteRepository {

	private List<Agente> agentes;
	private int cdAgente;
	
	public AgenteRepositoryTest() {
		this.agentes = new ArrayList<Agente>();
		this.cdAgente = 1;
	}

	@Override
	public void insert(Agente agente, CustomConnection customConnection) throws Exception {
		agente.setCdAgente(this.cdAgente++);
		this.agentes.add(agente);
	}

	@Override
	public void update(Agente agente, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.agentes.size(); i++) {
			Agente agenteFromList = this.agentes.get(i);
			if(agenteFromList.getCdAgente() == agente.getCdAgente()) {
				this.agentes.set(i, agente);
				break;
			}
		}
	}

	@Override
	public Agente get(int cdAgente, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.agentes.size(); i++) {
			Agente agenteFromList = this.agentes.get(i);
			if(agenteFromList.getCdAgente() == cdAgente) {
				return agenteFromList;
			}
		}
		return null;
	}

	@Override
	public Agente getByCdUsuario(int cdUsuario) throws Exception {
		return null;
	}

	@Override
	public Agente getByCdUsuario(int cdUsuario, CustomConnection customConnection) throws Exception {
		return null;
	}

	@Override
	public List<Agente> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.agentes;
	}

	@Override
	public Agente get(int cdAgente) throws Exception {
		return get(cdAgente);
	}

	@Override
	public List<Agente> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios);
	}

	@Override
	public void delete(int cdAgente, CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
