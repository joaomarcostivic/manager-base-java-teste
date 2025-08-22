package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RrdOrgaoDAO{

	public static int insert(RrdOrgao objeto) {
		return insert(objeto, null);
	}

	public static int insert(RrdOrgao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_rrd_orgao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRrdOrgao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_rrd_orgao (cd_rrd_orgao,"+
			                                  "id_rrd_orgao,"+
			                                  "nm_rrd_orgao,"+
			                                  "st_rrd_orgao) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdRrdOrgao());
			pstmt.setString(3,objeto.getNmRrdOrgao());
			pstmt.setInt(4,objeto.getStRrdOrgao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RrdOrgao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(RrdOrgao objeto, int cdRrdOrgaoOld) {
		return update(objeto, cdRrdOrgaoOld, null);
	}

	public static int update(RrdOrgao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(RrdOrgao objeto, int cdRrdOrgaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_rrd_orgao SET cd_rrd_orgao=?,"+
												      		   "id_rrd_orgao=?,"+
												      		   "nm_rrd_orgao=?,"+
												      		   "st_rrd_orgao=? WHERE cd_rrd_orgao=?");
			pstmt.setInt(1,objeto.getCdRrdOrgao());
			pstmt.setString(2,objeto.getIdRrdOrgao());
			pstmt.setString(3,objeto.getNmRrdOrgao());
			pstmt.setInt(4,objeto.getStRrdOrgao());
			pstmt.setInt(5, cdRrdOrgaoOld!=0 ? cdRrdOrgaoOld : objeto.getCdRrdOrgao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRrdOrgao) {
		return delete(cdRrdOrgao, null);
	}

	public static int delete(int cdRrdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_rrd_orgao WHERE cd_rrd_orgao=?");
			pstmt.setInt(1, cdRrdOrgao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RrdOrgao get(int cdRrdOrgao) {
		return get(cdRrdOrgao, null);
	}

	public static RrdOrgao get(int cdRrdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_rrd_orgao WHERE cd_rrd_orgao=?");
			pstmt.setInt(1, cdRrdOrgao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RrdOrgao(rs.getInt("cd_rrd_orgao"),
						rs.getString("id_rrd_orgao"),
						rs.getString("nm_rrd_orgao"),
						rs.getInt("st_rrd_orgao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_rrd_orgao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RrdOrgao> getList() {
		return getList(null);
	}

	public static ArrayList<RrdOrgao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RrdOrgao> list = new ArrayList<RrdOrgao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RrdOrgao obj = RrdOrgaoDAO.get(rsm.getInt("cd_rrd_orgao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdOrgaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_rrd_orgao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}