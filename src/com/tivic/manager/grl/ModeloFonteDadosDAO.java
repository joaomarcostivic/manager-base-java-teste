package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ModeloFonteDadosDAO{

	public static int insert(ModeloFonteDados objeto) {
		return insert(objeto, null);
	}

	public static int insert(ModeloFonteDados objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_modelo_fonte_dados (cd_modelo,"+
			                                  "cd_fonte,"+
			                                  "cd_fonte_pai) VALUES (?, ?, ?)");
			if(objeto.getCdModelo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdModelo());
			if(objeto.getCdFonte()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFonte());
			if(objeto.getCdFontePai()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFontePai());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ModeloFonteDados objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ModeloFonteDados objeto, int cdModeloOld, int cdFonteOld) {
		return update(objeto, cdModeloOld, cdFonteOld, null);
	}

	public static int update(ModeloFonteDados objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ModeloFonteDados objeto, int cdModeloOld, int cdFonteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_modelo_fonte_dados SET cd_modelo=?,"+
												      		   "cd_fonte=?,"+
												      		   "cd_fonte_pai=? WHERE cd_modelo=? AND cd_fonte=?");
			pstmt.setInt(1,objeto.getCdModelo());
			pstmt.setInt(2,objeto.getCdFonte());
			if(objeto.getCdFontePai()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFontePai());
			pstmt.setInt(4, cdModeloOld!=0 ? cdModeloOld : objeto.getCdModelo());
			pstmt.setInt(5, cdFonteOld!=0 ? cdFonteOld : objeto.getCdFonte());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdModelo, int cdFonte) {
		return delete(cdModelo, cdFonte, null);
	}

	public static int delete(int cdModelo, int cdFonte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_modelo_fonte_dados WHERE cd_modelo=? AND cd_fonte=?");
			pstmt.setInt(1, cdModelo);
			pstmt.setInt(2, cdFonte);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ModeloFonteDados get(int cdModelo, int cdFonte) {
		return get(cdModelo, cdFonte, null);
	}

	public static ModeloFonteDados get(int cdModelo, int cdFonte, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_modelo_fonte_dados WHERE cd_modelo=? AND cd_fonte=?");
			pstmt.setInt(1, cdModelo);
			pstmt.setInt(2, cdFonte);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ModeloFonteDados(rs.getInt("cd_modelo"),
						rs.getInt("cd_fonte"),
						rs.getInt("cd_fonte_pai"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_modelo_fonte_dados");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloFonteDadosDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_modelo_fonte_dados", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
