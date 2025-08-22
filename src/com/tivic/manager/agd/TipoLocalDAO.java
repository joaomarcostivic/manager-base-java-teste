package com.tivic.manager.agd;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;

import java.util.ArrayList;

public class TipoLocalDAO{

	public static int insert(TipoLocal objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoLocal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int code = Conexao.getSequenceCode("agd_tipo_local", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoLocal(code);
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_tipo_local (cd_tipo_local,"+
			                                  "nm_tipo_local,"+
			                                  "id_tipo_local) VALUES (?, ?, ?)");
			pstmt.setInt(1,objeto.getCdTipoLocal());
			pstmt.setString(2,objeto.getNmTipoLocal());
			pstmt.setString(3,objeto.getIdTipoLocal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoLocal objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoLocal objeto, int cdTipoLocalOld) {
		return update(objeto, cdTipoLocalOld, null);
	}

	public static int update(TipoLocal objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoLocal objeto, int cdTipoLocalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE AGD_TIPO_LOCAL SET CD_TIPO_LOCAL=?,"+
												      		   "NM_TIPO_LOCAL=?,"+ 
												      		   "ID_TIPO_LOCAL=? WHERE CD_TIPO_LOCAL=?");
			pstmt.setInt(1,objeto.getCdTipoLocal());
			pstmt.setString(2,objeto.getNmTipoLocal());
			pstmt.setString(3,objeto.getIdTipoLocal());
			pstmt.setInt(4, cdTipoLocalOld!=0 ? cdTipoLocalOld : objeto.getCdTipoLocal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoLocal) {
		return delete(cdTipoLocal, null);
	}

	public static int delete(int cdTipoLocal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM AGD_TIPO_LOCAL WHERE CD_TIPO_LOCAL=?");
			pstmt.setInt(1, cdTipoLocal);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoLocal get(int cdTipoLocal) {
		return get(cdTipoLocal, null);
	}

	public static TipoLocal get(int cdTipoLocal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM AGD_TIPO_LOCAL WHERE CD_TIPO_LOCAL=?");
			pstmt.setInt(1, cdTipoLocal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoLocal(rs.getInt("CD_TIPO_LOCAL"),
						rs.getString("NM_TIPO_LOCAL"),
						rs.getString("ID_TIPO_LOCAL"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM AGD_TIPO_LOCAL");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoLocal> getList() {
		return getList(null);
	}

	public static ArrayList<TipoLocal> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoLocal> list = new ArrayList<TipoLocal>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoLocal obj = TipoLocalDAO.get(rsm.getInt("CD_TIPO_LOCAL"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoLocalDAO.getList: " + e);
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
		return Search.find("SELECT * FROM AGD_TIPO_LOCAL", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
