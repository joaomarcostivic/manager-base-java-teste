package com.tivic.manager.crt;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class MotivoDAO{

	public static int insert(Motivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Motivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("CRT_MOTIVO");
			pstmt = connect.prepareStatement("INSERT INTO CRT_MOTIVO (CD_MOTIVO,"+
			                                  "NM_MOTIVO) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmMotivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Motivo objeto) {
		return update(objeto, null);
	}

	public static int update(Motivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE CRT_MOTIVO SET NM_MOTIVO=? WHERE CD_MOTIVO=?");
			pstmt.setString(1,objeto.getNmMotivo());
			pstmt.setInt(2,objeto.getCdMotivo());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMotivo) {
		return delete(cdMotivo, null);
	}

	public static int delete(int cdMotivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM CRT_MOTIVO WHERE CD_MOTIVO=?");
			pstmt.setInt(1, cdMotivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Motivo get(int cdMotivo) {
		return get(cdMotivo, null);
	}

	public static Motivo get(int cdMotivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM CRT_MOTIVO WHERE CD_MOTIVO=?");
			pstmt.setInt(1, cdMotivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Motivo(rs.getInt("CD_MOTIVO"),
						rs.getString("NM_MOTIVO"));
			}
			else{
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM CRT_MOTIVO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM CRT_MOTIVO", criterios, Conexao.conectar());
	}

}