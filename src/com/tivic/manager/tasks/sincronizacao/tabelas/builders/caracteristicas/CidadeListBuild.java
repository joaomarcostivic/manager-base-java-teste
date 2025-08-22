package com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.grl.Cidade;

public class CidadeListBuild implements ISincronizadorJsonBuilder<Cidade> {

private List<Cidade> cidades;
	
	public CidadeListBuild() {
		cidades = new ArrayList<Cidade>();
	}
	
	@Override
	public CidadeListBuild process(JSONArray data) throws Exception {
		for(int i = 0; i < data.length(); i++) {
			JSONObject cidadeJson = data.getJSONObject(i);
			Cidade cidade = new Cidade();
			cidade.setCdCidade(cidadeJson.getInt("cdCidade"));
			cidade.setNmCidade(cidadeJson.getString("nmCidade"));
			cidade.setIdCidade(cidadeJson.getString("idCidade"));
			cidades.add(cidade);
		}
		return this;
	}

	@Override
	public List<Cidade> build() {
		return cidades;
	}

}
