package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.connection.creater.CreaterConnectionCustom;

public class DatabaseConnectionManager {
	
	public void configConnection(CustomConnection customConnection, boolean isTransaction) throws Exception {
		if(ManagerConf.getInstance().get("POSTGRESQL_HOST_DBPATH_BASE_NOVA") == null) {
			setDefaultDb(customConnection, isTransaction);
		} else {
			setCustomDb(customConnection, isTransaction);
		}
	}
	
	private void setCustomDb(CustomConnection customConnection, boolean isTransaction) throws Exception {
		customConnection.initConnection(isTransaction,
				new CreaterConnectionCustom(ManagerConf.getInstance().get("POSTGRESQL_HOST_DBPATH_BASE_NOVA"),
						ManagerConf.getInstance().get("POSTGRESQL_LOGIN_BASE_NOVA"),
						ManagerConf.getInstance().get("POSTGRESQL_SENHA_BASE_NOVA")).create());
	}
	
	private void setDefaultDb(CustomConnection customConnection, boolean isTransaction) throws Exception {
		customConnection.initConnection(isTransaction);
	}
}
