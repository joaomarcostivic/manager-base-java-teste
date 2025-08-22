package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class RegraDAO{

	public static int insert(Regra objeto) {
		return insert(objeto, null);
	}

	public static int insert(Regra objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_regra", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegra(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_regra (cd_regra,nm_regra,tp_regra) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmRegra());
			pstmt.setInt(3,objeto.getTpRegra());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Regra objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Regra objeto, int cdRegraOld) {
		return update(objeto, cdRegraOld, null);
	}

	public static int update(Regra objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Regra objeto, int cdRegraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_regra SET cd_regra=?,"+
												      		   "nm_regra=?,"+
												      		   "tp_regra=? WHERE cd_regra=?");
			pstmt.setInt(1,objeto.getCdRegra());
			pstmt.setString(2,objeto.getNmRegra());
			pstmt.setInt(3,objeto.getTpRegra());
			pstmt.setInt(4, cdRegraOld!=0 ? cdRegraOld : objeto.getCdRegra());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegra) {
		return delete(cdRegra, null);
	}

	public static int delete(int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_regra WHERE cd_regra=?");
			pstmt.setInt(1, cdRegra);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Regra get(int cdRegra) {
		return get(cdRegra, null);
	}

	public static Regra get(int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_regra WHERE cd_regra=?");
			pstmt.setInt(1, cdRegra);
			rs = pstmt.executeQuery();
			if(rs.next())
				return new Regra(rs.getInt("cd_regra"),rs.getString("nm_regra"),rs.getInt("tp_regra"));
			
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_regra ORDER BY nm_regra");
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM crm_regra", "ORDER BY nm_regra", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
