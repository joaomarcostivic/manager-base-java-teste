package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AitEventoDAO{

	public static int insert(AitEvento objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitEvento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_evento (cd_ait,"+
			                                  "cd_evento) VALUES (?, ?)");
			if(objeto.getCdAit()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAit());
			if(objeto.getCdEvento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitEvento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitEvento objeto, int cdAitOld, int cdEventoOld) {
		return update(objeto, cdAitOld, cdEventoOld, null);
	}

	public static int update(AitEvento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitEvento objeto, int cdAitOld, int cdEventoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_evento SET cd_ait=?,"+
												      		   "cd_evento=? WHERE cd_ait=? AND cd_evento=?");
			pstmt.setInt(1,objeto.getCdAit());
			pstmt.setInt(2,objeto.getCdEvento());
			pstmt.setInt(3, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.setInt(4, cdEventoOld!=0 ? cdEventoOld : objeto.getCdEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAit, int cdEvento) {
		return delete(cdAit, cdEvento, null);
	}

	public static int delete(int cdAit, int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_evento WHERE cd_ait=? AND cd_evento=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitEvento get(int cdAit, int cdEvento) {
		return get(cdAit, cdEvento, null);
	}

	public static AitEvento get(int cdAit, int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_evento WHERE cd_ait=? AND cd_evento=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitEvento(rs.getInt("cd_ait"),
						rs.getInt("cd_evento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitEvento> getList() {
		return getList(null);
	}

	public static ArrayList<AitEvento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitEvento> list = new ArrayList<AitEvento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitEvento obj = AitEventoDAO.get(rsm.getInt("cd_ait"), rsm.getInt("cd_evento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitEventoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait_evento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
