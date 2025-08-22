package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FormaContatoDAO{

	public static int insert(FormaContato objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormaContato objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_forma_contato", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormaContato(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_forma_contato (cd_forma_contato,"+
			                                  "nm_forma_contato) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFormaContato());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaContatoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaContatoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormaContato objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FormaContato objeto, int cdFormaContatoOld) {
		return update(objeto, cdFormaContatoOld, null);
	}

	public static int update(FormaContato objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FormaContato objeto, int cdFormaContatoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_forma_contato SET cd_forma_contato=?,"+
												      		   "nm_forma_contato=? WHERE cd_forma_contato=?");
			pstmt.setInt(1,objeto.getCdFormaContato());
			pstmt.setString(2,objeto.getNmFormaContato());
			pstmt.setInt(3, cdFormaContatoOld!=0 ? cdFormaContatoOld : objeto.getCdFormaContato());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaContatoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaContatoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormaContato) {
		return delete(cdFormaContato, null);
	}

	public static int delete(int cdFormaContato, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_forma_contato WHERE cd_forma_contato=?");
			pstmt.setInt(1, cdFormaContato);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaContatoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaContatoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormaContato get(int cdFormaContato) {
		return get(cdFormaContato, null);
	}

	public static FormaContato get(int cdFormaContato, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_forma_contato WHERE cd_forma_contato=?");
			pstmt.setInt(1, cdFormaContato);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormaContato(rs.getInt("cd_forma_contato"),
						rs.getString("nm_forma_contato"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaContatoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaContatoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_forma_contato ORDER BY nm_forma_contato");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaContatoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_forma_contato", "ORDER BY nm_forma_contato", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
