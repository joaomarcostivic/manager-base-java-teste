package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

import com.tivic.sol.connection.Conexao;

public class NcmServices {
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_unidade_medida FROM grl_ncm A " + 
											 " JOIN grl_unidade_medida B ON(A.cd_unidade_medida = B.cd_unidade_medida)");
			return new ResultSetMap(pstmt.executeQuery());
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
	
	public static ResultSetMap findNrNcm(String nrNcm) {
		return findNrNcm(nrNcm, 0, null);
	}

	public static ResultSetMap findNrNcm(String nrNcm, int cdEmpresa) {
		return findNrNcm(nrNcm, cdEmpresa, null);
	}

	public static ResultSetMap findNrNcm(String nrNcm, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstm = connect.prepareStatement("SELECT A.*, B.cd_classificacao_fiscal FROM grl_ncm A " +
					                                          "LEFT OUTER JOIN adm_ncm B ON (A.cd_ncm = B.cd_ncm) "+
					                                          "WHERE A.nr_ncm = '"+nrNcm+"' ");
			return new ResultSetMap(pstm.executeQuery());
			
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		ResultSetMap rsm = Search.find("SELECT A.*, B.cd_classificacao_fiscal FROM grl_ncm A " +
		           "LEFT OUTER JOIN adm_ncm B ON (A.cd_ncm = B.cd_ncm) " +
		           "WHERE 1=1 ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		return rsm;
	}
}
