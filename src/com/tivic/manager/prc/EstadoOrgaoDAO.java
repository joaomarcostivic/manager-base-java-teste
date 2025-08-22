package com.tivic.manager.prc;

import com.tivic.sol.connection.Conexao;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class EstadoOrgaoDAO{

	public static int insert(EstadoOrgao objeto) {
		return insert(objeto, null);
	}

	public static int insert(EstadoOrgao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_ESTADO_ORGAO (CD_ESTADO,"+
			                                  "CD_ORGAO) VALUES (?, ?)");
			if(objeto.getCdEstado()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEstado());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOrgao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EstadoOrgao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(EstadoOrgao objeto, int cdEstadoOld, int cdOrgaoOld) {
		return update(objeto, cdEstadoOld, cdOrgaoOld, null);
	}

	public static int update(EstadoOrgao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(EstadoOrgao objeto, int cdEstadoOld, int cdOrgaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_ESTADO_ORGAO SET CD_ESTADO=?,"+
												      		   "CD_ORGAO=? WHERE CD_ESTADO=? AND CD_ORGAO=?");
			pstmt.setInt(1,objeto.getCdEstado());
			pstmt.setInt(2,objeto.getCdOrgao());
			pstmt.setInt(3, cdEstadoOld!=0 ? cdEstadoOld : objeto.getCdEstado());
			pstmt.setInt(4, cdOrgaoOld!=0 ? cdOrgaoOld : objeto.getCdOrgao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEstado, int cdOrgao) {
		return delete(cdEstado, cdOrgao, null);
	}

	public static int delete(int cdEstado, int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_ESTADO_ORGAO WHERE CD_ESTADO=? AND CD_ORGAO=?");
			pstmt.setInt(1, cdEstado);
			pstmt.setInt(2, cdOrgao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EstadoOrgao get(int cdEstado, int cdOrgao) {
		return get(cdEstado, cdOrgao, null);
	}

	public static EstadoOrgao get(int cdEstado, int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_ESTADO_ORGAO WHERE CD_ESTADO=? AND CD_ORGAO=?");
			pstmt.setInt(1, cdEstado);
			pstmt.setInt(2, cdOrgao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EstadoOrgao(rs.getInt("CD_ESTADO"),
						rs.getInt("CD_ORGAO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_ESTADO_ORGAO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EstadoOrgao> getList() {
		return getList(null);
	}

	public static ArrayList<EstadoOrgao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EstadoOrgao> list = new ArrayList<EstadoOrgao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EstadoOrgao obj = EstadoOrgaoDAO.get(rsm.getInt("CD_ESTADO"), rsm.getInt("CD_ORGAO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_ESTADO_ORGAO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
