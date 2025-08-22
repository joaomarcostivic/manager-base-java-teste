package com.tivic.manager.srh;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CategoriaFgtsDAO{

	public static int insert(CategoriaFgts objeto) {
		return insert(objeto, null);
	}

	public static int insert(CategoriaFgts objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_categoria_fgts", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCategoriaFgts(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_categoria_fgts (cd_categoria_fgts,"+
			                                  "nm_categoria_fgts,"+
			                                  "id_categoria_fgts) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCategoriaFgts());
			pstmt.setString(3,objeto.getIdCategoriaFgts());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CategoriaFgts objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CategoriaFgts objeto, int cdCategoriaFgtsOld) {
		return update(objeto, cdCategoriaFgtsOld, null);
	}

	public static int update(CategoriaFgts objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CategoriaFgts objeto, int cdCategoriaFgtsOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_categoria_fgts SET cd_categoria_fgts=?,"+
												      		   "nm_categoria_fgts=?,"+
												      		   "id_categoria_fgts=? WHERE cd_categoria_fgts=?");
			pstmt.setInt(1,objeto.getCdCategoriaFgts());
			pstmt.setString(2,objeto.getNmCategoriaFgts());
			pstmt.setString(3,objeto.getIdCategoriaFgts());
			pstmt.setInt(4, cdCategoriaFgtsOld!=0 ? cdCategoriaFgtsOld : objeto.getCdCategoriaFgts());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCategoriaFgts) {
		return delete(cdCategoriaFgts, null);
	}

	public static int delete(int cdCategoriaFgts, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_categoria_fgts WHERE cd_categoria_fgts=?");
			pstmt.setInt(1, cdCategoriaFgts);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CategoriaFgts get(int cdCategoriaFgts) {
		return get(cdCategoriaFgts, null);
	}

	public static CategoriaFgts get(int cdCategoriaFgts, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_categoria_fgts WHERE cd_categoria_fgts=?");
			pstmt.setInt(1, cdCategoriaFgts);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CategoriaFgts(rs.getInt("cd_categoria_fgts"),
						rs.getString("nm_categoria_fgts"),
						rs.getString("id_categoria_fgts"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_categoria_fgts");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CategoriaFgts> getList() {
		return getList(null);
	}

	public static ArrayList<CategoriaFgts> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CategoriaFgts> list = new ArrayList<CategoriaFgts>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CategoriaFgts obj = CategoriaFgtsDAO.get(rsm.getInt("cd_categoria_fgts"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgtsDAO.getList: " + e);
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
		return Search.find("SELECT * FROM srh_categoria_fgts", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
