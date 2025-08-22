package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class LacreDAO{

	public static int insert(Lacre objeto) {
		return insert(objeto, null);
	}

	public static int insert(Lacre objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_lacre", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLacre(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_lacre (cd_lacre,"+
			                                  "id_lacre,"+
			                                  "st_lacre,"+
			                                  "id_serie) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdLacre());
			pstmt.setInt(3,objeto.getStLacre());
			pstmt.setString(4,objeto.getIdSerie());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Lacre objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Lacre objeto, int cdLacreOld) {
		return update(objeto, cdLacreOld, null);
	}

	public static int update(Lacre objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Lacre objeto, int cdLacreOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_lacre SET cd_lacre=?,"+
												      		   "id_lacre=?,"+
												      		   "st_lacre=?,"+
												      		   "id_serie=? WHERE cd_lacre=?");
			pstmt.setInt(1,objeto.getCdLacre());
			pstmt.setString(2,objeto.getIdLacre());
			pstmt.setInt(3,objeto.getStLacre());
			pstmt.setString(4,objeto.getIdSerie());
			pstmt.setInt(5, cdLacreOld!=0 ? cdLacreOld : objeto.getCdLacre());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLacre) {
		return delete(cdLacre, null);
	}

	public static int delete(int cdLacre, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_lacre WHERE cd_lacre=?");
			pstmt.setInt(1, cdLacre);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Lacre get(int cdLacre) {
		return get(cdLacre, null);
	}

	public static Lacre get(int cdLacre, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lacre WHERE cd_lacre=?");
			pstmt.setInt(1, cdLacre);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Lacre(rs.getInt("cd_lacre"),
						rs.getString("id_lacre"),
						rs.getInt("st_lacre"),
						rs.getString("id_serie"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_lacre");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Lacre> getList() {
		return getList(null);
	}

	public static ArrayList<Lacre> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Lacre> list = new ArrayList<Lacre>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Lacre obj = LacreDAO.get(rsm.getInt("cd_lacre"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_lacre", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
