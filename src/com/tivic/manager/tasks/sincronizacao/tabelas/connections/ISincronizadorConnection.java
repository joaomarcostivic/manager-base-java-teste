package com.tivic.manager.tasks.sincronizacao.tabelas.connections;

import com.tivic.sol.httpclient.HttpEntity;

public interface ISincronizadorConnection {
	public HttpEntity connect(String tableName) throws Exception, IllegalStateException;
}
