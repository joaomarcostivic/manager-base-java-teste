package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CategoriaCnhDAO{

	public static int insert(CategoriaCnh objeto) {
		return insert(objeto, null);
	}

	public static int insert(CategoriaCnh objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_categoria_cnh", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCategoriaCnh(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_categoria_cnh (cd_categoria_cnh,"+
			                                  "nm_categoria) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCategoria());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CategoriaCnh objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CategoriaCnh objeto, int cdCategoriaCnhOld) {
		return update(objeto, cdCategoriaCnhOld, null);
	}

	public static int update(CategoriaCnh objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CategoriaCnh objeto, int cdCategoriaCnhOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_categoria_cnh SET cd_categoria_cnh=?,"+
												      		   "nm_categoria=? WHERE cd_categoria_cnh=?");
			pstmt.setInt(1,objeto.getCdCategoriaCnh());
			pstmt.setString(2,objeto.getNmCategoria());
			pstmt.setInt(3, cdCategoriaCnhOld!=0 ? cdCategoriaCnhOld : objeto.getCdCategoriaCnh());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCategoriaCnh) {
		return delete(cdCategoriaCnh, null);
	}

	public static int delete(int cdCategoriaCnh, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_categoria_cnh WHERE cd_categoria_cnh=?");
			pstmt.setInt(1, cdCategoriaCnh);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CategoriaCnh get(int cdCategoriaCnh) {
		return get(cdCategoriaCnh, null);
	}

	public static CategoriaCnh get(int cdCategoriaCnh, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_categoria_cnh WHERE cd_categoria_cnh=?");
			pstmt.setInt(1, cdCategoriaCnh);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CategoriaCnh(rs.getInt("cd_categoria_cnh"),
						rs.getString("nm_categoria"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_categoria_cnh");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CategoriaCnh> getList() {
		return getList(null);
	}

	public static ArrayList<CategoriaCnh> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CategoriaCnh> list = new ArrayList<CategoriaCnh>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CategoriaCnh obj = CategoriaCnhDAO.get(rsm.getInt("cd_categoria_cnh"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaCnhDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_categoria_cnh", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}