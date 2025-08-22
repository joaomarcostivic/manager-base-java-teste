package com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.fta.EspecieVeiculo;

public class EspecieVeiculoListBuilder implements ISincronizadorJsonBuilder<EspecieVeiculo> {
	
	private List<EspecieVeiculo> especiesVeiculos;
	
	public EspecieVeiculoListBuilder() {
		especiesVeiculos = new ArrayList<EspecieVeiculo>();
	}
	
	@Override
	public EspecieVeiculoListBuilder process(JSONArray data) throws Exception {
		for(int i = 0; i < data.length(); i++) {
			JSONObject especieVeiculoJson = data.getJSONObject(i);
			EspecieVeiculo especieVeiculo = new EspecieVeiculo();
			especieVeiculo.setCdEspecie(especieVeiculoJson.getInt("cdEspecie"));
			especieVeiculo.setDsEspecie(especieVeiculoJson.getString("dsEspecie"));
			especiesVeiculos.add(especieVeiculo);
		}
		return this;
	}

	@Override
	public List<EspecieVeiculo> build() {
		return especiesVeiculos;
	}

}
