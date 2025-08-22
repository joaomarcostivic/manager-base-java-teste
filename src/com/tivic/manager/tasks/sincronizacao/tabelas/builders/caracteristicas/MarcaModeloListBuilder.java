package com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.sol.util.date.DateUtil;
import com.tivic.sol.util.date.conversors.IsoFormat;

public class MarcaModeloListBuilder implements ISincronizadorJsonBuilder<MarcaModelo> {

	private List<MarcaModelo> marcasModelos;
	
	public MarcaModeloListBuilder() {
		marcasModelos = new ArrayList<MarcaModelo>();
	}
	
	@Override
	public MarcaModeloListBuilder process(JSONArray data) throws Exception{
		for(int i = 0; i < data.length(); i++) {
			JSONObject marcasJson = data.getJSONObject(i);
			MarcaModelo marcaModelo = new MarcaModelo();
			marcaModelo.setCdMarca(marcasJson.getInt("cdMarca"));
			
			if(marcasJson.has("nmMarca") && !marcasJson.isNull("nmMarca") && !marcasJson.getString("nmMarca").isEmpty()) {
				marcaModelo.setNmMarca(marcasJson.getString("nmMarca"));
			}
			
			if (marcasJson.has("nmModelo") && !marcasJson.isNull("nmModelo") && !marcasJson.getString("nmModelo").isEmpty()) {
			    marcaModelo.setNmModelo(marcasJson.getString("nmModelo"));
			}
						
			if (marcasJson.has("tpMarca") && !marcasJson.isNull("tpMarca")) {
	            marcaModelo.setTpMarca(marcasJson.getInt("tpMarca"));
	        } 
			
			if (marcasJson.has("dtAtualizacao") && !marcasJson.isNull("dtAtualizacao")) 
				marcaModelo.setDtAtualizacao(DateUtil.convStringToCalendar(marcasJson.getString("dtAtualizacao"), new IsoFormat()));
				
			marcasModelos.add(marcaModelo);
		}
		return this;
	}
	
	@Override
	public List<MarcaModelo> build() {
		return marcasModelos;
	}
}
