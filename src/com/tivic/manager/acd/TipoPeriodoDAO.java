package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoPeriodoDAO{

	public static int insert(TipoPeriodo objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoPeriodo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_periodo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoPeriodo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_periodo (cd_tipo_periodo,"+
			                                  "nm_tipo_periodo,"+
			                                  "st_tipo_periodo,"+
			                                  "id_tipo_periodo) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoPeriodo());
			pstmt.setInt(3,objeto.getStTipoPeriodo());
			pstmt.setString(4,objeto.getIdTipoPeriodo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoPeriodo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoPeriodo objeto, int cdTipoPeriodoOld) {
		return update(objeto, cdTipoPeriodoOld, null);
	}

	public static int update(TipoPeriodo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoPeriodo objeto, int cdTipoPeriodoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_periodo SET cd_tipo_periodo=?,"+
												      		   "nm_tipo_periodo=?,"+
												      		   "st_tipo_periodo=?,"+
												      		   "id_tipo_periodo=? WHERE cd_tipo_periodo=?");
			pstmt.setInt(1,objeto.getCdTipoPeriodo());
			pstmt.setString(2,objeto.getNmTipoPeriodo());
			pstmt.setInt(3,objeto.getStTipoPeriodo());
			pstmt.setString(4,objeto.getIdTipoPeriodo());
			pstmt.setInt(5, cdTipoPeriodoOld!=0 ? cdTipoPeriodoOld : objeto.getCdTipoPeriodo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoPeriodo) {
		return delete(cdTipoPeriodo, null);
	}

	public static int delete(int cdTipoPeriodo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_periodo WHERE cd_tipo_periodo=?");
			pstmt.setInt(1, cdTipoPeriodo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoPeriodo get(int cdTipoPeriodo) {
		return get(cdTipoPeriodo, null);
	}

	public static TipoPeriodo get(int cdTipoPeriodo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_periodo WHERE cd_tipo_periodo=?");
			pstmt.setInt(1, cdTipoPeriodo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoPeriodo(rs.getInt("cd_tipo_periodo"),
						rs.getString("nm_tipo_periodo"),
						rs.getInt("st_tipo_periodo"),
						rs.getString("id_tipo_periodo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_periodo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoPeriodo> getList() {
		return getList(null);
	}

	public static ArrayList<TipoPeriodo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoPeriodo> list = new ArrayList<TipoPeriodo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoPeriodo obj = TipoPeriodoDAO.get(rsm.getInt("cd_tipo_periodo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_periodo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
