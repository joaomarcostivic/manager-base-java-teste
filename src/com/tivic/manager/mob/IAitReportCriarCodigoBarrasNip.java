package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.HashMap;

import sol.dao.ResultSetMap;

public interface IAitReportCriarCodigoBarrasNip {
	String criarCodigoBarras(ResultSetMap rsm, HashMap<String, Object> paramns, Connection connect);
}
