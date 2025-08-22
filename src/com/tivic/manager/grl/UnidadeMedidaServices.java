package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class UnidadeMedidaServices {

	public static int delete(int cdUnidadeMedida) {
		return delete(cdUnidadeMedida, null);
	}

	public static int delete(int cdUnidadeMedida, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PreparedStatement pstmt = connection.prepareStatement("DELETE " +
					"FROM grl_unidade_conversao " +
					"WHERE cd_unidade_origem = ?");
			pstmt.setInt(1, cdUnidadeMedida);
			pstmt.execute();

			if (UnidadeMedidaDAO.delete(cdUnidadeMedida, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllConversoes(int cdUnidadeMedida) {
		return getAllConversoes(cdUnidadeMedida, null);
	}

	public static ResultSetMap getAllConversoes(int cdUnidadeMedida, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_unidade_medida AS nm_unidade_destino, " +
					" B.sg_unidade_medida AS sg_unidade_destino " +
					"FROM GRL_UNIDADE_CONVERSAO A, grl_unidade_medida B " +
					"WHERE A.cd_unidade_destino = B.cd_unidade_medida " +
					"  AND A.cd_unidade_origem = ?");
			pstmt.setInt(1, cdUnidadeMedida);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeMedidaServices.getAllConversoes: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, A.cd_unidade_medida AS cd_unidade_medida_produto, A.nm_unidade_medida AS nm_unidade_medida_produto FROM grl_unidade_medida A ORDER BY A.nm_unidade_medida");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeConversaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
