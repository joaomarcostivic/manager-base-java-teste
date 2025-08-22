package com.tivic.manager.bpm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MarcaDAO{

	public static int insert(Marca objeto) {
		return insert(objeto, null);
	}

	public static int insert(Marca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("bpm_marca", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMarca(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bpm_marca (cd_marca,"+
			                                  "nm_marca,"+
			                                  "id_marca) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmMarca());
			pstmt.setString(3,objeto.getIdMarca());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Marca objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Marca objeto, int cdMarcaOld) {
		return update(objeto, cdMarcaOld, null);
	}

	public static int update(Marca objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Marca objeto, int cdMarcaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bpm_marca SET cd_marca=?,"+
												      		   "nm_marca=?,"+
												      		   "id_marca=? WHERE cd_marca=?");
			pstmt.setInt(1,objeto.getCdMarca());
			pstmt.setString(2,objeto.getNmMarca());
			pstmt.setString(3,objeto.getIdMarca());
			pstmt.setInt(4, cdMarcaOld!=0 ? cdMarcaOld : objeto.getCdMarca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMarca) {
		return delete(cdMarca, null);
	}

	public static int delete(int cdMarca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bpm_marca WHERE cd_marca=?");
			pstmt.setInt(1, cdMarca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Marca get(int cdMarca) {
		return get(cdMarca, null);
	}

	public static Marca get(int cdMarca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_marca WHERE cd_marca=?");
			pstmt.setInt(1, cdMarca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Marca(rs.getInt("cd_marca"),
						rs.getString("nm_marca"),
						rs.getString("id_marca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bpm_marca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Marca> getList() {
		return getList(null);
	}

	public static ArrayList<Marca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Marca> list = new ArrayList<Marca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Marca obj = MarcaDAO.get(rsm.getInt("cd_marca"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM bpm_marca", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}