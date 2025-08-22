package com.tivic.manager.sinc;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class LoteDAO{

	public static int insert(Lote objeto) {
		return insert(objeto, null);
	}

	public static int insert(Lote objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int code = 0;
			ArrayList<HashMap<String, Object>> keys = Conexao.getPrimaryKeysNames("sinc_lote", connect);
			if (keys != null && keys.size() == 1 && ((Boolean)keys.get(0).get("IS_NATIVE_KEY")).booleanValue()) {
				ResultSet rs = connect.prepareStatement("SELECT MAX(" + keys.get(0).get("NM_KEY") + ") " +
						                         "FROM sinc_lote").executeQuery();
				code = rs.next() ? rs.getInt(1) + 1 : 1;
			}
			
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLote(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO sinc_lote (cd_lote,"+
			                                  "dt_lote) VALUES (?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtLote()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLote().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Lote objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Lote objeto, int cdLoteOld) {
		return update(objeto, cdLoteOld, null);
	}

	public static int update(Lote objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Lote objeto, int cdLoteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE sinc_lote SET cd_lote=?,"+
												      		   "dt_lote=? WHERE cd_lote=?");
			pstmt.setInt(1,objeto.getCdLote());
			if(objeto.getDtLote()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLote().getTimeInMillis()));
			pstmt.setInt(3, cdLoteOld!=0 ? cdLoteOld : objeto.getCdLote());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLote) {
		return delete(cdLote, null);
	}

	public static int delete(int cdLote, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM sinc_lote WHERE cd_lote=?");
			pstmt.setInt(1, cdLote);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Lote get(int cdLote) {
		return get(cdLote, null);
	}

	public static Lote get(int cdLote, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM sinc_lote WHERE cd_lote=?");
			pstmt.setInt(1, cdLote);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Lote(rs.getInt("cd_lote"),
						(rs.getTimestamp("dt_lote")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lote").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_lote");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Lote> getList() {
		return getList(null);
	}

	public static ArrayList<Lote> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Lote> list = new ArrayList<Lote>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Lote obj = LoteDAO.get(rsm.getInt("cd_lote"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM sinc_lote", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
