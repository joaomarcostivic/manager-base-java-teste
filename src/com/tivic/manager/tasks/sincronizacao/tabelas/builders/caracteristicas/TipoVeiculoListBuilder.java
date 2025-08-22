package com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.fta.TipoVeiculo;

public class TipoVeiculoListBuilder implements ISincronizadorJsonBuilder<TipoVeiculo>{

private List<TipoVeiculo> tipoVeiculo;
	
	public TipoVeiculoListBuilder() {
		tipoVeiculo = new ArrayList<TipoVeiculo>();
	}
	
	@Override
	public TipoVeiculoListBuilder process(JSONArray data) throws Exception {
		for(int i = 0; i < data.length(); i++) {
			JSONObject tipoJson = data.getJSONObject(i);
			TipoVeiculo tipo = new TipoVeiculo();
			tipo.setCdTipoVeiculo(tipoJson.getInt("cdTipo"));
			tipo.setNmTipoVeiculo(tipoJson.getString("nmTipo"));
			
			if (tipoJson.has("txtCnhRequerida") && !tipoJson.isNull("txtCnhRequerida")) 
				tipo.setTxtCnhRequerida(tipoJson.getString("txtCnhRequerida"));
	        
			tipoVeiculo.add(tipo);
		}
		return this;
	}

	@Override
	public List<TipoVeiculo> build() {
		return tipoVeiculo;
	}

}
