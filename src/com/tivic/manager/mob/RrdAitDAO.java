package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RrdAitDAO{

	public static int insert(RrdAit objeto) {
		return insert(objeto, null);
	}

	public static int insert(RrdAit objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_rrd_ait (cd_ait,"+
			                                  "cd_rrd,"+
			                                  "nr_rrd,"+
			                                  "nr_ait) VALUES (?, ?, ?, ?)");
			if(objeto.getCdAit()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAit());
			if(objeto.getCdRrd()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRrd());
			pstmt.setInt(3,objeto.getNrRrd());
			pstmt.setString(4,objeto.getNrAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RrdAit objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RrdAit objeto, int cdAitOld, int cdRrdOld) {
		return update(objeto, cdAitOld, cdRrdOld, null);
	}

	public static int update(RrdAit objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RrdAit objeto, int cdAitOld, int cdRrdOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_rrd_ait SET cd_ait=?,"+
												      		   "cd_rrd=?,"+
												      		   "nr_rrd=?,"+
												      		   "nr_ait=? WHERE cd_ait=? AND cd_rrd=?");
			pstmt.setInt(1,objeto.getCdAit());
			pstmt.setInt(2,objeto.getCdRrd());
			pstmt.setInt(3,objeto.getNrRrd());
			pstmt.setString(4,objeto.getNrAit());
			pstmt.setInt(5, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.setInt(6, cdRrdOld!=0 ? cdRrdOld : objeto.getCdRrd());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAit, int cdRrd) {
		return delete(cdAit, cdRrd, null);
	}

	public static int delete(int cdAit, int cdRrd, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_rrd_ait WHERE cd_ait=? AND cd_rrd=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdRrd);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RrdAit get(int cdAit, int cdRrd) {
		return get(cdAit, cdRrd, null);
	}

	public static RrdAit get(int cdAit, int cdRrd, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_rrd_ait WHERE cd_ait=? AND cd_rrd=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdRrd);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RrdAit(rs.getInt("cd_ait"),
						rs.getInt("cd_rrd"),
						rs.getInt("nr_rrd"),
						rs.getString("nr_ait"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_rrd_ait");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RrdAit> getList() {
		return getList(null);
	}

	public static ArrayList<RrdAit> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RrdAit> list = new ArrayList<RrdAit>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RrdAit obj = RrdAitDAO.get(rsm.getInt("cd_ait"), rsm.getInt("cd_rrd"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdAitDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_rrd_ait", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
