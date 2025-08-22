package com.tivic.manager.grl.estado;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class EstadoRepositoryDAO implements EstadoRepository {

    @Override
    public Estado insert(Estado estado, CustomConnection customConnection) throws Exception {
        int cdEstado = EstadoDAO.insert(estado, customConnection.getConnection());
        if(cdEstado <= 0)
            throw new Exception("erro ao inserir Estado");
        estado.setCdEstado(cdEstado);
        return estado;
    }

    @Override
    public Estado update(Estado estado, CustomConnection customConnection) throws Exception {
        int cdEstado = EstadoDAO.update(estado, customConnection.getConnection());
    	if(cdEstado <= 0)
    		throw new Exception("Erro em alteração de estado");
        return estado;
    }
    
    @Override
	public Estado get(int cdEstado) throws Exception {
		return EstadoDAO.get(cdEstado);
	}

    @Override
    public Estado get(int cdEstado, CustomConnection customConnection) throws Exception {
        return EstadoDAO.get(cdEstado, customConnection.getConnection());
    }
    
    @Override
    public List<Estado> getAll() throws Exception {
    	return getAll(new CustomConnection());
    }
	
	@Override
	public List<Estado> getAll(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<Estado> search = new SearchBuilder<Estado>("grl_estado")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Estado.class);
	}

    @Override
	public List<Estado> find(ArrayList<ItemComparator> criterios) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.setCriterios(criterios);
		Search<Estado> search = new SearchBuilder<Estado>("grl_estado")
				.searchCriterios(searchCriterios)
				.build();
		return search.getList(Estado.class);
	}

	@Override
	public List<Estado> find(ArrayList<ItemComparator> criterios, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.setCriterios(criterios);
		Search<Estado> search = new SearchBuilder<Estado>("grl_estado")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(Estado.class);
	}
}