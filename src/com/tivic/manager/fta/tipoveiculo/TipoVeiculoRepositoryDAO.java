package com.tivic.manager.fta.tipoveiculo;

import java.util.List;

import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.TipoVeiculoDAO;
import com.tivic.manager.fta.tipoveiculo.TipoVeiculoRepository;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class TipoVeiculoRepositoryDAO implements TipoVeiculoRepository {

	@Override
	public void insert(TipoVeiculo tipoVeiculo, CustomConnection customConnection) throws Exception {
		int cdTipoVeiculo = TipoVeiculoDAO.insert(tipoVeiculo, customConnection.getConnection());
		if (cdTipoVeiculo < 0)
			throw new Exception("Erro ao inserir TipoVeiculo.");
		tipoVeiculo.setCdTipoVeiculo(cdTipoVeiculo);	
	}

	@Override
	public void update(TipoVeiculo tipoVeiculo, CustomConnection customConnection) throws Exception {
		TipoVeiculoDAO.update(tipoVeiculo, customConnection.getConnection());
	}

	@Override
	public TipoVeiculo get(int cdTipoVeiculo) throws Exception {
		return get(cdTipoVeiculo, new CustomConnection());
	}

	@Override
	public TipoVeiculo get(int cdTipoVeiculo, CustomConnection customConnection) throws Exception {
		return TipoVeiculoDAO.get(cdTipoVeiculo, customConnection.getConnection());
	}

	@Override
	public List<TipoVeiculo> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<TipoVeiculo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<TipoVeiculo> search = new SearchBuilder<TipoVeiculo>("fta_tipo_veiculo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(TipoVeiculo.class);
	} 
}
