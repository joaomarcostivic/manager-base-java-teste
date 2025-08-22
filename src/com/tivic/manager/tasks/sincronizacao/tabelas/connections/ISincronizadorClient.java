package com.tivic.manager.tasks.sincronizacao.tabelas.connections;

import org.json.JSONObject;

public interface ISincronizadorClient {
	public JSONObject getData(String table) throws Exception, IllegalStateException;
}
