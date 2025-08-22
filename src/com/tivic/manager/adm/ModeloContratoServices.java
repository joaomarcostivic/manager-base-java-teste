package com.tivic.manager.adm;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ModeloContratoServices {

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.*, B.nm_modelo AS nm_modelo_contrato " +
						   "FROM adm_modelo_contrato A, grl_modelo_documento B " +
						   "WHERE A.cd_modelo_contrato = B.cd_modelo ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.*, B.nm_modelo AS nm_modelo_contrato " +
					                                         "FROM adm_modelo_contrato A, grl_modelo_documento B " +
					                                         "WHERE A.cd_modelo_contrato = B.cd_modelo " +
					                                         "ORDER BY nm_modelo").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


}