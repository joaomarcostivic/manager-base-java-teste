package com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

import org.json.JSONObject;

import com.tivic.manager.fta.Cor;

public class CorListBuilder implements ISincronizadorJsonBuilder<Cor> {

	private List<Cor> cores;
	
	public CorListBuilder() {
		cores = new ArrayList<Cor>();
	}
	
	@Override
	public CorListBuilder process(JSONArray data) throws Exception{
		for(int i = 0; i < data.length(); i++) {
			JSONObject corJson = data.getJSONObject(i);
			Cor cor = new Cor();
			cor.setCdCor(corJson.getInt("cdCor"));
			cor.setNmCor(corJson.getString("nmCor"));
			cores.add(cor);
		}
		return this;
	}
	
	@Override
	public List<Cor> build() {
		return cores;
	}


	

}
