package com.tivic.manager.acd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AreaInteresseDAO{

	public static int insert(AreaInteresse objeto) {
		return insert(objeto, null);
	}

	public static int insert(AreaInteresse objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_area_interesse", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAreaInteresse(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_area_interesse (cd_area_interesse,"+
			                                  "nm_area_interesse,"+
			                                  "id_area_interesse) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAreaInteresse());
			pstmt.setString(3,objeto.getIdAreaInteresse());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaInteresseDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaInteresseDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AreaInteresse objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AreaInteresse objeto, int cdAreaInteresseOld) {
		return update(objeto, cdAreaInteresseOld, null);
	}

	public static int update(AreaInteresse objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AreaInteresse objeto, int cdAreaInteresseOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_area_interesse SET cd_area_interesse=?,"+
												      		   "nm_area_interesse=?,"+
												      		   "id_area_interesse=? WHERE cd_area_interesse=?");
			pstmt.setInt(1,objeto.getCdAreaInteresse());
			pstmt.setString(2,objeto.getNmAreaInteresse());
			pstmt.setString(3,objeto.getIdAreaInteresse());
			pstmt.setInt(4, cdAreaInteresseOld!=0 ? cdAreaInteresseOld : objeto.getCdAreaInteresse());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaInteresseDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaInteresseDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAreaInteresse) {
		return delete(cdAreaInteresse, null);
	}

	public static int delete(int cdAreaInteresse, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_area_interesse WHERE cd_area_interesse=?");
			pstmt.setInt(1, cdAreaInteresse);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaInteresseDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaInteresseDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AreaInteresse get(int cdAreaInteresse) {
		return get(cdAreaInteresse, null);
	}

	public static AreaInteresse get(int cdAreaInteresse, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_area_interesse WHERE cd_area_interesse=?");
			pstmt.setInt(1, cdAreaInteresse);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AreaInteresse(rs.getInt("cd_area_interesse"),
						rs.getString("nm_area_interesse"),
						rs.getString("id_area_interesse"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaInteresseDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaInteresseDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_area_interesse");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaInteresseDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaInteresseDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_area_interesse", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
