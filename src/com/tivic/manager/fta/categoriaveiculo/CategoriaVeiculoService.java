package com.tivic.manager.fta.categoriaveiculo;

import java.util.List;

import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

public class CategoriaVeiculoService implements ICategoriaVeiculoService {

	private CategoriaVeiculoRepository categoriaVeiculoRepository;
	
	public CategoriaVeiculoService() throws Exception {
		this.categoriaVeiculoRepository = (CategoriaVeiculoRepository) BeansFactory.get(CategoriaVeiculoRepository.class);
	}
	
	@Override
	public CategoriaVeiculo getByNome(String nmCategoria) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_categoria", nmCategoria, true);
		List<CategoriaVeiculo> categoriasVeiculo = this.categoriaVeiculoRepository.find(searchCriterios);
		if(categoriasVeiculo.isEmpty())
			throw new Exception("Nenhuma categoria de veiculo encontrada");
		return categoriasVeiculo.get(0);
	}

}
