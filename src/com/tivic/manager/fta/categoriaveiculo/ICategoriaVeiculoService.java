package com.tivic.manager.fta.categoriaveiculo;

import com.tivic.manager.fta.CategoriaVeiculo;

public interface ICategoriaVeiculoService {
	public CategoriaVeiculo getByNome(String nmCategoria) throws Exception;
}
