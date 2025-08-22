package com.tivic.manager.fta.categoriaveiculo;

import java.util.List;

import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface CategoriaVeiculoRepository {
	public void insert(CategoriaVeiculo categoriaVeiculo, CustomConnection customConnection) throws Exception;
	public void update(CategoriaVeiculo categoriaVeiculo, CustomConnection customConnection) throws Exception;
	public CategoriaVeiculo get(int cdCategoriaVeiculo) throws Exception;
	public CategoriaVeiculo get(int cdCategoriaVeiculo, CustomConnection customConnection) throws Exception;
	public List<CategoriaVeiculo> find(SearchCriterios searchCriterios) throws Exception;
	public List<CategoriaVeiculo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
