package com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.fta.CategoriaVeiculo;

public class CategoriaVeiculoListBuilder implements ISincronizadorJsonBuilder<CategoriaVeiculo> {

	private List<CategoriaVeiculo> categoriaVeiculos;
	
	public CategoriaVeiculoListBuilder() {
		categoriaVeiculos = new ArrayList<CategoriaVeiculo>();
	}
	
	@Override
	public CategoriaVeiculoListBuilder process(JSONArray data) throws Exception {
		for(int i = 0; i < data.length(); i++) {
			JSONObject corJson = data.getJSONObject(i);
			CategoriaVeiculo categoriaVeiculo = new CategoriaVeiculo();
			categoriaVeiculo.setCdCategoria(corJson.getInt("cdCategoria"));
			categoriaVeiculo.setNmCategoria(corJson.getString("nmCategoria"));
			categoriaVeiculos.add(categoriaVeiculo);
		}
		return this;
	}

	@Override
	public List<CategoriaVeiculo> build() {
		return categoriaVeiculos;
	}

}
