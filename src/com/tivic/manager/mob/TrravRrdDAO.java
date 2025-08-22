package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TrravRrdDAO{

	public static int insert(TrravRrd objeto) {
		return insert(objeto, null);
	}

	public static int insert(TrravRrd objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_trrav_rrd (cd_rrd,"+
			                                  "cd_trrav) VALUES (?, ?)");
			if(objeto.getCdRrd()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRrd());
			if(objeto.getCdTrrav()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTrrav());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TrravRrd objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TrravRrd objeto, int cdRrdOld, int cdTrravOld) {
		return update(objeto, cdRrdOld, cdTrravOld, null);
	}

	public static int update(TrravRrd objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TrravRrd objeto, int cdRrdOld, int cdTrravOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_trrav_rrd SET cd_rrd=?,"+
												      		   "cd_trrav=? WHERE cd_rrd=? AND cd_trrav=?");
			pstmt.setInt(1,objeto.getCdRrd());
			pstmt.setInt(2,objeto.getCdTrrav());
			pstmt.setInt(3, cdRrdOld!=0 ? cdRrdOld : objeto.getCdRrd());
			pstmt.setInt(4, cdTrravOld!=0 ? cdTrravOld : objeto.getCdTrrav());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRrd, int cdTrrav) {
		return delete(cdRrd, cdTrrav, null);
	}

	public static int delete(int cdRrd, int cdTrrav, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_trrav_rrd WHERE cd_rrd=? AND cd_trrav=?");
			pstmt.setInt(1, cdRrd);
			pstmt.setInt(2, cdTrrav);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TrravRrd get(int cdRrd, int cdTrrav) {
		return get(cdRrd, cdTrrav, null);
	}

	public static TrravRrd get(int cdRrd, int cdTrrav, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_trrav_rrd WHERE cd_rrd=? AND cd_trrav=?");
			pstmt.setInt(1, cdRrd);
			pstmt.setInt(2, cdTrrav);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TrravRrd(rs.getInt("cd_rrd"),
						rs.getInt("cd_trrav"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_trrav_rrd");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TrravRrd> getList() {
		return getList(null);
	}

	public static ArrayList<TrravRrd> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TrravRrd> list = new ArrayList<TrravRrd>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TrravRrd obj = TrravRrdDAO.get(rsm.getInt("cd_rrd"), rsm.getInt("cd_trrav"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_trrav_rrd", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}