package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class BoatInconsistenciaDAO{

	public static int insert(BoatInconsistencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(BoatInconsistencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_boat_inconsistencia (cd_boat,"+
			                                  "cd_inconsistencia) VALUES (?, ?)");
			if(objeto.getCdBoat()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdBoat());
			if(objeto.getCdInconsistencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInconsistencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BoatInconsistencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(BoatInconsistencia objeto, int cdBoatOld, int cdInconsistenciaOld) {
		return update(objeto, cdBoatOld, cdInconsistenciaOld, null);
	}

	public static int update(BoatInconsistencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(BoatInconsistencia objeto, int cdBoatOld, int cdInconsistenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_boat_inconsistencia SET cd_boat=?,"+
												      		   "cd_inconsistencia=? WHERE cd_boat=? AND cd_inconsistencia=?");
			pstmt.setInt(1,objeto.getCdBoat());
			pstmt.setInt(2,objeto.getCdInconsistencia());
			pstmt.setInt(3, cdBoatOld!=0 ? cdBoatOld : objeto.getCdBoat());
			pstmt.setInt(4, cdInconsistenciaOld!=0 ? cdInconsistenciaOld : objeto.getCdInconsistencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBoat, int cdInconsistencia) {
		return delete(cdBoat, cdInconsistencia, null);
	}

	public static int delete(int cdBoat, int cdInconsistencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_boat_inconsistencia WHERE cd_boat=? AND cd_inconsistencia=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdInconsistencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BoatInconsistencia get(int cdBoat, int cdInconsistencia) {
		return get(cdBoat, cdInconsistencia, null);
	}

	public static BoatInconsistencia get(int cdBoat, int cdInconsistencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_inconsistencia WHERE cd_boat=? AND cd_inconsistencia=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdInconsistencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BoatInconsistencia(rs.getInt("cd_boat"),
						rs.getInt("cd_inconsistencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_inconsistencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<BoatInconsistencia> getList() {
		return getList(null);
	}

	public static ArrayList<BoatInconsistencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<BoatInconsistencia> list = new ArrayList<BoatInconsistencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				BoatInconsistencia obj = BoatInconsistenciaDAO.get(rsm.getInt("cd_boat"), rsm.getInt("cd_inconsistencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_boat_inconsistencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
