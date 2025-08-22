package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class DoencaDAO{

	public static int insert(Doenca objeto) {
		return insert(objeto, null);
	}

	public static int insert(Doenca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_doenca", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDoenca(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_doenca (cd_doenca,"+
			                                  "nm_doenca,"+
			                                  "id_doenca,"+
			                                  "tp_doenca) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmDoenca());
			pstmt.setString(3,objeto.getIdDoenca());
			pstmt.setInt(4,objeto.getTpDoenca());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Doenca objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Doenca objeto, int cdDoencaOld) {
		return update(objeto, cdDoencaOld, null);
	}

	public static int update(Doenca objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Doenca objeto, int cdDoencaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_doenca SET cd_doenca=?,"+
												      		   "nm_doenca=?,"+
												      		   "id_doenca=?,"+
												      		   "tp_doenca=? WHERE cd_doenca=?");
			pstmt.setInt(1,objeto.getCdDoenca());
			pstmt.setString(2,objeto.getNmDoenca());
			pstmt.setString(3,objeto.getIdDoenca());
			pstmt.setInt(4,objeto.getTpDoenca());
			pstmt.setInt(5, cdDoencaOld!=0 ? cdDoencaOld : objeto.getCdDoenca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDoenca) {
		return delete(cdDoenca, null);
	}

	public static int delete(int cdDoenca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_doenca WHERE cd_doenca=?");
			pstmt.setInt(1, cdDoenca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Doenca get(int cdDoenca) {
		return get(cdDoenca, null);
	}

	public static Doenca get(int cdDoenca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_doenca WHERE cd_doenca=?");
			pstmt.setInt(1, cdDoenca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Doenca(rs.getInt("cd_doenca"),
						rs.getString("nm_doenca"),
						rs.getString("id_doenca"),
						rs.getInt("tp_doenca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_doenca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Doenca> getList() {
		return getList(null);
	}

	public static ArrayList<Doenca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Doenca> list = new ArrayList<Doenca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Doenca obj = DoencaDAO.get(rsm.getInt("cd_doenca"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.getList: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Doenca getById(String idDoenca) {
		return getById(idDoenca, null);
	}

	public static Doenca getById(String idDoenca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_doenca WHERE id_doenca=?");
			pstmt.setString(1, idDoenca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Doenca(rs.getInt("cd_doenca"),
						rs.getString("nm_doenca"),
						rs.getString("id_doenca"),
						rs.getInt("tp_doenca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DoencaDAO.get: " + e);
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
		return Search.find("SELECT * FROM grl_doenca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
