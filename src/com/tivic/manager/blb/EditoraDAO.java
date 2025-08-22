package com.tivic.manager.blb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class EditoraDAO{

	public static int insert(Editora objeto) {
		return insert(objeto, null);
	}

	public static int insert(Editora objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("blb_editora", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEditora(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_editora (cd_editora,"+
			                                  "nm_editora,"+
			                                  "id_editora) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmEditora());
			pstmt.setString(3,objeto.getIdEditora());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Editora objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Editora objeto, int cdEditoraOld) {
		return update(objeto, cdEditoraOld, null);
	}

	public static int update(Editora objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Editora objeto, int cdEditoraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_editora SET cd_editora=?,"+
												      		   "nm_editora=?,"+
												      		   "id_editora=? WHERE cd_editora=?");
			pstmt.setInt(1,objeto.getCdEditora());
			pstmt.setString(2,objeto.getNmEditora());
			pstmt.setString(3,objeto.getIdEditora());
			pstmt.setInt(4, cdEditoraOld!=0 ? cdEditoraOld : objeto.getCdEditora());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEditora) {
		return delete(cdEditora, null);
	}

	public static int delete(int cdEditora, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_editora WHERE cd_editora=?");
			pstmt.setInt(1, cdEditora);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Editora get(int cdEditora) {
		return get(cdEditora, null);
	}

	public static Editora get(int cdEditora, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_editora WHERE cd_editora=?");
			pstmt.setInt(1, cdEditora);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Editora(rs.getInt("cd_editora"),
						rs.getString("nm_editora"),
						rs.getString("id_editora"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_editora");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Editora> getList() {
		return getList(null);
	}

	public static ArrayList<Editora> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Editora> list = new ArrayList<Editora>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Editora obj = EditoraDAO.get(rsm.getInt("cd_editora"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EditoraDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_editora", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
