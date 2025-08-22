package com.tivic.manager.tasks.sincronizacao.tabelas.connections;

import com.tivic.sol.httpclient.HttpEntity;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.sol.httpclient.HttpEntityBuilder;

public class SincronizadorConnection implements ISincronizadorConnection{

	 @Override
	    public HttpEntity connect(String tableName) throws Exception, IllegalStateException {
	        String apiPath = ManagerConf.getInstance().get("SINCRONIZADOR_API_PATH");

	        if (apiPath == null) {
	            throw new IllegalStateException("A propriedade 'SINCRONIZADOR_API_PATH' não está definida no manager.conf");
	        }

	        String url = apiPath + tableName;

	        return new HttpEntityBuilder()
	                .url(url)
	                .build();
	    }

}
