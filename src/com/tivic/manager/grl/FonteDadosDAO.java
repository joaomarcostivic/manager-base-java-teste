package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FonteDadosDAO{

	public static int insert(FonteDados objeto) {
		return insert(objeto, null);
	}

	public static int insert(FonteDados objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_fonte_dados", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFonte(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_fonte_dados (cd_fonte,"+
			                                  "nm_fonte,"+
			                                  "txt_fonte,"+
			                                  "txt_script,"+
			                                  "txt_columns,"+
			                                  "id_fonte,"+
			                                  "tp_origem,"+
			                                  "tp_fonte) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFonte());
			pstmt.setString(3,objeto.getTxtFonte());
			pstmt.setString(4,objeto.getTxtScript());
			pstmt.setString(5,objeto.getTxtColumns());
			pstmt.setString(6,objeto.getIdFonte());
			pstmt.setInt(7,objeto.getTpOrigem());
			pstmt.setInt(8,objeto.getTpFonte());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FonteDados objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FonteDados objeto, int cdFonteOld) {
		return update(objeto, cdFonteOld, null);
	}

	public static int update(FonteDados objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FonteDados objeto, int cdFonteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_fonte_dados SET cd_fonte=?,"+
												      		   "nm_fonte=?,"+
												      		   "txt_fonte=?,"+
												      		   "txt_script=?,"+
												      		   "txt_columns=?,"+
												      		   "id_fonte=?,"+
												      		   "tp_origem=?,"+
												      		   "tp_fonte=? WHERE cd_fonte=?");
			pstmt.setInt(1,objeto.getCdFonte());
			pstmt.setString(2,objeto.getNmFonte());
			pstmt.setString(3,objeto.getTxtFonte());
			pstmt.setString(4,objeto.getTxtScript());
			pstmt.setString(5,objeto.getTxtColumns());
			pstmt.setString(6,objeto.getIdFonte());
			pstmt.setInt(7,objeto.getTpOrigem());
			pstmt.setInt(8,objeto.getTpFonte());
			pstmt.setInt(9, cdFonteOld!=0 ? cdFonteOld : objeto.getCdFonte());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFonte) {
		return delete(cdFonte, null);
	}

	public static int delete(int cdFonte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_fonte_dados WHERE cd_fonte=?");
			pstmt.setInt(1, cdFonte);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FonteDados get(int cdFonte) {
		return get(cdFonte, null);
	}

	public static FonteDados get(int cdFonte, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_fonte_dados WHERE cd_fonte=?");
			pstmt.setInt(1, cdFonte);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FonteDados(rs.getInt("cd_fonte"),
						rs.getString("nm_fonte"),
						rs.getString("txt_fonte"),
						rs.getString("txt_script"),
						rs.getString("txt_columns"),
						rs.getString("id_fonte"),
						rs.getInt("tp_origem"),
						rs.getInt("tp_fonte"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_fonte_dados");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FonteDadosDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_fonte_dados", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
