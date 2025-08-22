package com.tivic.manager.fta.especieveiculo;

import java.util.List;

import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.EspecieVeiculoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class EspecieVeiculoRepositoryDAO implements EspecieVeiculoRepository {

	@Override
	public void insert(EspecieVeiculo especieVeiculo, CustomConnection customConnection) throws Exception {
		int cdEspecie = EspecieVeiculoDAO.insert(especieVeiculo, customConnection.getConnection());
		if (cdEspecie < 0)
			throw new Exception("Erro ao inserir EspecieVeiculo.");
		especieVeiculo.setCdEspecie(cdEspecie);	
	}

	@Override
	public void update(EspecieVeiculo especieVeiculo, CustomConnection customConnection) throws Exception {
		EspecieVeiculoDAO.update(especieVeiculo, customConnection.getConnection());
	}

	@Override
	public EspecieVeiculo get(int cdEspecie) throws Exception {
		return get(cdEspecie, new CustomConnection());
	}

	@Override
	public EspecieVeiculo get(int cdEspecie, CustomConnection customConnection) throws Exception {
		return EspecieVeiculoDAO.get(cdEspecie, customConnection.getConnection());
	}

    @Override
    public List<EspecieVeiculo> getAll() throws Exception {
    	return getAll(new CustomConnection());
    }
	
	@Override
	public List<EspecieVeiculo> getAll(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<EspecieVeiculo> search = new SearchBuilder<EspecieVeiculo>("fta_marca_modelo")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(EspecieVeiculo.class);
	}
	
	@Override
	public List<EspecieVeiculo> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<EspecieVeiculo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<EspecieVeiculo> search = new SearchBuilder<EspecieVeiculo>("fta_especie_veiculo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(EspecieVeiculo.class);
	} 
}
