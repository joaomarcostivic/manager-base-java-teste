package com.tivic.manager.fta.categoriaveiculo;

import java.util.List;

import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.CategoriaVeiculoDAO;
import com.tivic.manager.fta.categoriaveiculo.CategoriaVeiculoRepository;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CategoriaVeiculoRepositoryDAO  implements CategoriaVeiculoRepository {

	@Override
	public void insert(CategoriaVeiculo categoriaVeiculo, CustomConnection customConnection) throws Exception {
		int cdCategoria = CategoriaVeiculoDAO.insert(categoriaVeiculo, customConnection.getConnection());
		if (cdCategoria <= 0)
			throw new Exception("Erro ao inserir CategoriaVeiculo.");
		categoriaVeiculo.setCdCategoria(cdCategoria);	
	}

	@Override
	public void update(CategoriaVeiculo categoriaVeiculo, CustomConnection customConnection) throws Exception {
		CategoriaVeiculoDAO.update(categoriaVeiculo, customConnection.getConnection());
	}

	@Override
	public CategoriaVeiculo get(int cdCategoriaVeiculo) throws Exception {
		return get(cdCategoriaVeiculo, new CustomConnection());
	}

	@Override
	public CategoriaVeiculo get(int cdCategoriaVeiculo, CustomConnection customConnection) throws Exception {
		return CategoriaVeiculoDAO.get(cdCategoriaVeiculo, customConnection.getConnection());
	}

	@Override
	public List<CategoriaVeiculo> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<CategoriaVeiculo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<CategoriaVeiculo> search = new SearchBuilder<CategoriaVeiculo>("fta_categoria_veiculo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(CategoriaVeiculo.class);
	} 
}
