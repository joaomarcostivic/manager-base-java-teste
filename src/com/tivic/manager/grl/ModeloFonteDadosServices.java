package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tivic.sol.connection.Conexao;


public class ModeloFonteDadosServices {

	public static int save(int cdModelo, int cdFonte, String nmFonte, String txtFonte, String txtScript, String txtColumns, 
			String idFonte, int tpOrigem, int tpFonte, int cdFontePai){
		return save(cdModelo, cdFonte, nmFonte, txtFonte, txtScript, txtColumns, idFonte, tpOrigem, tpFonte, cdFontePai, null);
	}
	
	public static int save(int cdModelo, int cdFonte, String nmFonte, String txtFonte, String txtScript, String txtColumns, 
			String idFonte, int tpOrigem, int tpFonte, int cdFontePai, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = FonteDadosServices.save(cdFonte, nmFonte, txtFonte, txtScript, txtColumns, idFonte, tpOrigem, tpFonte, connect);
			if (retorno <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			if (cdFonte <= 0) {
				if (ModeloFonteDadosDAO.insert(new ModeloFonteDados(cdModelo, retorno, cdFontePai), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}
			else if (ModeloFonteDadosDAO.update(new ModeloFonteDados(cdModelo, cdFonte, cdFontePai), connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int delete(int cdModelo, int cdFonte) {
		return delete(cdModelo, cdFonte, null);
	}

	public static int delete(int cdModelo, int cdFonte, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			if (ModeloFonteDadosDAO.delete(cdModelo, cdFonte, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM grl_modelo_fonte_dados " +
					"WHERE cd_fonte = ?");
			pstmt.setInt(1, cdFonte);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next())
				if (FonteDadosDAO.delete(cdFonte, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			
			if (isConnectionNull)
				connection.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
}
