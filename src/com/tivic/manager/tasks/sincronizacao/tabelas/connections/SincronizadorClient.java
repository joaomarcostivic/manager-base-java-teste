package com.tivic.manager.tasks.sincronizacao.tabelas.connections;

import org.json.JSONObject;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.httpclient.HttpEntity;
import com.tivic.sol.httpclient.ISolHttpClient;

public class SincronizadorClient implements ISincronizadorClient {

	private ISincronizadorConnection syncConnection;
	private ISolHttpClient solHttpClient;
	
	public SincronizadorClient() throws Exception {
		syncConnection = (ISincronizadorConnection) BeansFactory.get(ISincronizadorConnection.class);
		solHttpClient = (ISolHttpClient) BeansFactory.get(ISolHttpClient.class);
	}
	
	@Override
	public JSONObject getData(String table) throws Exception, IllegalStateException {
		HttpEntity http = syncConnection.connect(table);
		String jsonObject = (String) solHttpClient.get(http);
		return new JSONObject(jsonObject);
	}

}
