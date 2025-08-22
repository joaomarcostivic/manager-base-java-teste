package com.tivic.manager.tasks.sincronizacao.tabelas.services.consultas.api;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.tasks.sincronizacao.tabelas.connections.ISincronizadorClient;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.SincronizadorClient;
import com.tivic.sol.cdi.BeansFactory;

public class ConsultaSincronizadorCategoriaVeiculoService implements IConsultaSincronizadorService{
	private ISincronizadorClient sincronizadorClient;
	
	public ConsultaSincronizadorCategoriaVeiculoService() throws Exception {
		sincronizadorClient = (ISincronizadorClient) BeansFactory.get(ISincronizadorClient.class);
	}
	
	@Override
	public JSONArray consult() throws Exception {
		JSONObject jsonCategorias = sincronizadorClient.getData("categorias");
		return jsonCategorias.getJSONArray("categoriasVeiculo");
	}
}
