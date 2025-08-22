package com.tivic.manager.srh;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AgenteNocivoDAO{

	public static int insert(AgenteNocivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgenteNocivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_agente_nocivo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAgenteNocivo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_agente_nocivo (cd_agente_nocivo,"+
			                                  "nm_agente_nocivo,"+
			                                  "id_agente_nocivo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAgenteNocivo());
			pstmt.setString(3,objeto.getIdAgenteNocivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgenteNocivo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AgenteNocivo objeto, int cdAgenteNocivoOld) {
		return update(objeto, cdAgenteNocivoOld, null);
	}

	public static int update(AgenteNocivo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AgenteNocivo objeto, int cdAgenteNocivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_agente_nocivo SET cd_agente_nocivo=?,"+
												      		   "nm_agente_nocivo=?,"+
												      		   "id_agente_nocivo=? WHERE cd_agente_nocivo=?");
			pstmt.setInt(1,objeto.getCdAgenteNocivo());
			pstmt.setString(2,objeto.getNmAgenteNocivo());
			pstmt.setString(3,objeto.getIdAgenteNocivo());
			pstmt.setInt(4, cdAgenteNocivoOld!=0 ? cdAgenteNocivoOld : objeto.getCdAgenteNocivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgenteNocivo) {
		return delete(cdAgenteNocivo, null);
	}

	public static int delete(int cdAgenteNocivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_agente_nocivo WHERE cd_agente_nocivo=?");
			pstmt.setInt(1, cdAgenteNocivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgenteNocivo get(int cdAgenteNocivo) {
		return get(cdAgenteNocivo, null);
	}

	public static AgenteNocivo get(int cdAgenteNocivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_agente_nocivo WHERE cd_agente_nocivo=?");
			pstmt.setInt(1, cdAgenteNocivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgenteNocivo(rs.getInt("cd_agente_nocivo"),
						rs.getString("nm_agente_nocivo"),
						rs.getString("id_agente_nocivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_agente_nocivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AgenteNocivo> getList() {
		return getList(null);
	}

	public static ArrayList<AgenteNocivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AgenteNocivo> list = new ArrayList<AgenteNocivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AgenteNocivo obj = AgenteNocivoDAO.get(rsm.getInt("cd_agente_nocivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteNocivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM srh_agente_nocivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
