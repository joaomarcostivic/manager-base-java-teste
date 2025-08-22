package com.tivic.manager.adapter.base.antiga.agente;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.agente.AgenteRepository;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AgenteRepositoryOldDAO implements AgenteRepository {
	
	private IAdapterService<AgenteOld, Agente> adapterService;
	
	public AgenteRepositoryOldDAO() throws Exception {
		this.adapterService = new AdapterAgenteService();
	}
	
	@Override
	public void insert(Agente agente, CustomConnection customConnection) throws Exception {
		AgenteOld agenteOld = this.adapterService.toBaseAntiga(agente);
		int cdAgente = AgenteOldDAO.insert(agenteOld, customConnection.getConnection());
		if (cdAgente <= 0)
			throw new Exception("Erro ao inserir Agente.");
		agente.setCdAgente(cdAgente);	
	}

	@Override
	public void update(Agente agente, CustomConnection customConnection) throws Exception {
		AgenteOld agenteOld = this.adapterService.toBaseAntiga(agente);
		AgenteOldDAO.update(agenteOld, customConnection.getConnection());
	}
	
	@Override
	public Agente get(int cdAgente) throws Exception {
		return get(cdAgente, new CustomConnection());
	}

	@Override
	public Agente get(int cdAgente, CustomConnection customConnection) throws Exception {
		AgenteOld agenteOld = AgenteOldDAO.get(cdAgente, customConnection.getConnection());
		return this.adapterService.toBaseNova(agenteOld);
	}
	
	@Override
	public Agente getByCdUsuario(int cdUsuario) throws Exception {
	    return getByCdUsuario(cdUsuario, new CustomConnection());
	}

	@Override
	public Agente getByCdUsuario(int cdUsuario, CustomConnection customConnection) throws Exception {
	    AgenteOld agenteOld = AgenteOldDAO.getByCdUsuario(cdUsuario, customConnection.getConnection());
        return this.adapterService.toBaseNova(agenteOld);
	}
	
	@Override
	public List<Agente> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Agente> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Agente> agentes = new ArrayList<Agente>();
		ResultSetMapper<AgenteOld> rsm = new ResultSetMapper<AgenteOld>(AgenteOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), AgenteOld.class);
		List<AgenteOld> agenteOldList = rsm.toList();
		for (AgenteOld agenteOld : agenteOldList) {
			Agente agente = this.adapterService.toBaseNova(agenteOld);
			agentes.add(agente);
		}
		return agentes;
	}
	
	@Override
	public void delete(int cdAgente, CustomConnection customConnection) throws Exception {
		int agente = AgenteOldDAO.delete(cdAgente, customConnection.getConnection());
		if(agente <= 0) {
			throw new Exception("Erro ao excluir agente");
		}
	}
}
