package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class SistemaProcessoDAO{

	public static int insert(SistemaProcesso objeto) {
		return insert(objeto, null);
	}

	public static int insert(SistemaProcesso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_sistema_processo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdSistemaProcesso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_sistema_processo (cd_sistema_processo,"+
			                                  "nm_sistema_processo,"+
			                                  "ds_sistema_processo,"+
			                                  "url_sistema_processo) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmSistemaProcesso());
			pstmt.setString(3,objeto.getDsSistemaProcesso());
			pstmt.setString(4,objeto.getUrlSistemaProcesso());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(SistemaProcesso objeto) {
		return update(objeto, 0, null);
	}

	public static int update(SistemaProcesso objeto, int cdSistemaProcessoOld) {
		return update(objeto, cdSistemaProcessoOld, null);
	}

	public static int update(SistemaProcesso objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(SistemaProcesso objeto, int cdSistemaProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_sistema_processo SET cd_sistema_processo=?,"+
												      		   "nm_sistema_processo=?,"+
												      		   "ds_sistema_processo=?,"+
												      		   "url_sistema_processo=? WHERE cd_sistema_processo=?");
			pstmt.setInt(1,objeto.getCdSistemaProcesso());
			pstmt.setString(2,objeto.getNmSistemaProcesso());
			pstmt.setString(3,objeto.getDsSistemaProcesso());
			pstmt.setString(4,objeto.getUrlSistemaProcesso());
			pstmt.setInt(5, cdSistemaProcessoOld!=0 ? cdSistemaProcessoOld : objeto.getCdSistemaProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSistemaProcesso) {
		return delete(cdSistemaProcesso, null);
	}

	public static int delete(int cdSistemaProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_sistema_processo WHERE cd_sistema_processo=?");
			pstmt.setInt(1, cdSistemaProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static SistemaProcesso get(int cdSistemaProcesso) {
		return get(cdSistemaProcesso, null);
	}

	public static SistemaProcesso get(int cdSistemaProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_sistema_processo WHERE cd_sistema_processo=?");
			pstmt.setInt(1, cdSistemaProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new SistemaProcesso(rs.getInt("cd_sistema_processo"),
						rs.getString("nm_sistema_processo"),
						rs.getString("ds_sistema_processo"),
						rs.getString("url_sistema_processo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_sistema_processo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<SistemaProcesso> getList() {
		return getList(null);
	}

	public static ArrayList<SistemaProcesso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<SistemaProcesso> list = new ArrayList<SistemaProcesso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				SistemaProcesso obj = SistemaProcessoDAO.get(rsm.getInt("cd_sistema_processo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_sistema_processo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
