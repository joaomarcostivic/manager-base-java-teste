package com.tivic.manager.importacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class AitMigrator {
	
	public static void main(String[] args) {
				
	}
	
	private static Connection getSourceConnection() {
		return Conexao.conectar();
	}
	
	private static void disconnect(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
	}
	
	private static Connection getDestinationConnection() {
		return Conexao.conectar("url", "login", "senha");
	}
	
	private static ResultSetMap getAits() {
		Connection conn = getSourceConnection();
		try {
			PreparedStatement ps = conn
					.prepareStatement("SELECT * from ait");
			return new ResultSetMap(ps.executeQuery());
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			disconnect(conn);
		}
	}
	

}
