package com.tivic.manager.tasks.sincronizacao.tabelas.services.consultas.api;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.tasks.sincronizacao.tabelas.connections.ISincronizadorClient;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.SincronizadorClient;
import com.tivic.sol.cdi.BeansFactory;

public class ConsultaSincronizadorEspecieVeiculoService implements IConsultaSincronizadorService {

	private ISincronizadorClient sincronizadorClient;
	
	public ConsultaSincronizadorEspecieVeiculoService() throws Exception {
		sincronizadorClient = (ISincronizadorClient) BeansFactory.get(ISincronizadorClient.class);
	}
	
	@Override
	public JSONArray consult() throws Exception {
		JSONObject jsonCores = sincronizadorClient.getData("especies");
		return jsonCores.getJSONArray("especiesVeiculo");
	}

}
