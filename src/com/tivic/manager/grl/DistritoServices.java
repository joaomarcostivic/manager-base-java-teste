package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class DistritoServices {

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_cidade, C.sg_estado " +
				"FROM grl_distrito A " +
				"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) " +
				"LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static HashMap<String,Object> getDistritoAndCidade(int cdDistrito, int cdCidade) {
		return getDistritoAndCidade(cdDistrito, cdCidade, null);
	}

	public static HashMap<String,Object> getDistritoAndCidade(int cdDistrito, int cdCidade, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			HashMap<String,Object> distritoAndCidade = new HashMap<String,Object>();
			distritoAndCidade.put("distrito", DistritoDAO.get(cdDistrito, cdCidade, connection));
			distritoAndCidade.put("cidade", CidadeDAO.get(cdCidade, connection));
			return distritoAndCidade;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllBairrosOfDistrito(int cdDistrito, int cdCidade) {
		return getAllBairrosOfDistrito(cdDistrito, cdCidade, null);
	}

	public static ResultSetMap getAllBairrosOfDistrito(int cdDistrito, int cdCidade, Connection connection) {
		boolean isConnectioNull = connection==null;
		try {
			if (isConnectioNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_bairro WHERE cd_cidade = ? AND cd_distrito = ?");
			pstmt.setInt(1, cdCidade);
			pstmt.setInt(2, cdDistrito);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectioNull)
				Conexao.desconectar(connection);
		}
	}

}