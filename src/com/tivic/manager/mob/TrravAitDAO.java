package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TrravAitDAO{

	public static int insert(TrravAit objeto) {
		return insert(objeto, null);
	}

	public static int insert(TrravAit objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_trrav_ait (cd_trrav,"+
			                                  "cd_ait,"+
			                                  "nr_trrav,"+
			                                  "nr_ait) VALUES (?, ?, ?, ?)");
			if(objeto.getCdTrrav()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTrrav());
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			pstmt.setInt(3,objeto.getNrTrrav());
			pstmt.setString(4,objeto.getNrAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TrravAit objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TrravAit objeto, int cdTrravOld, int cdAitOld) {
		return update(objeto, cdTrravOld, cdAitOld, null);
	}

	public static int update(TrravAit objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TrravAit objeto, int cdTrravOld, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_trrav_ait SET cd_trrav=?,"+
												      		   "cd_ait=?,"+
												      		   "nr_trrav=?,"+
												      		   "nr_ait=? WHERE cd_trrav=? AND cd_ait=?");
			pstmt.setInt(1,objeto.getCdTrrav());
			pstmt.setInt(2,objeto.getCdAit());
			pstmt.setInt(3,objeto.getNrTrrav());
			pstmt.setString(4,objeto.getNrAit());
			pstmt.setInt(5, cdTrravOld!=0 ? cdTrravOld : objeto.getCdTrrav());
			pstmt.setInt(6, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTrrav, int cdAit) {
		return delete(cdTrrav, cdAit, null);
	}

	public static int delete(int cdTrrav, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_trrav_ait WHERE cd_trrav=? AND cd_ait=?");
			pstmt.setInt(1, cdTrrav);
			pstmt.setInt(2, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TrravAit get(int cdTrrav, int cdAit) {
		return get(cdTrrav, cdAit, null);
	}

	public static TrravAit get(int cdTrrav, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_trrav_ait WHERE cd_trrav=? AND cd_ait=?");
			pstmt.setInt(1, cdTrrav);
			pstmt.setInt(2, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TrravAit(rs.getInt("cd_trrav"),
						rs.getInt("cd_ait"),
						rs.getInt("nr_trrav"),
						rs.getString("nr_ait"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_trrav_ait");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TrravAit> getList() {
		return getList(null);
	}

	public static ArrayList<TrravAit> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TrravAit> list = new ArrayList<TrravAit>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TrravAit obj = TrravAitDAO.get(rsm.getInt("cd_trrav"), rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravAitDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_trrav_ait", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
