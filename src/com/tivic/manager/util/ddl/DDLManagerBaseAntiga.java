package com.tivic.manager.util.ddl;

import java.sql.SQLException;

public class DDLManagerBaseAntiga implements DDLManager {

	DDLCore ddlCore = new DDLCore();
	String caminho = "/com/tivic/manager/conf/ddl_base_antiga.xml";
	
	@Override
	public void executar() throws SQLException, Exception {
		ddlCore.executar(caminho);
	}

}
