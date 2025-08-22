package com.tivic.manager.util.ddl;

import java.sql.SQLException;

public class DDLManagerBaseNova implements DDLManager {

	DDLCore ddlCore = new DDLCore();
	String caminho = "/com/tivic/manager/conf/ddl.xml";
	
	@Override
	public void executar() throws SQLException, Exception {
		ddlCore.executar(caminho);
	}

}
