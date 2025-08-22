package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class InconsistenciaDAO{

	public static int insert(Inconsistencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(Inconsistencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_inconsistencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdInconsistencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_inconsistencia (cd_inconsistencia,"+
			                                  "nm_inconsistencia,"+
			                                  "tp_inconsistencia) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmInconsistencia());
			pstmt.setInt(3,objeto.getTpInconsistencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Inconsistencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Inconsistencia objeto, int cdInconsistenciaOld) {
		return update(objeto, cdInconsistenciaOld, null);
	}

	public static int update(Inconsistencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Inconsistencia objeto, int cdInconsistenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_inconsistencia SET cd_inconsistencia=?,"+
												      		   "nm_inconsistencia=?,"+
												      		   "tp_inconsistencia=? WHERE cd_inconsistencia=?");
			pstmt.setInt(1,objeto.getCdInconsistencia());
			pstmt.setString(2,objeto.getNmInconsistencia());
			pstmt.setInt(3,objeto.getTpInconsistencia());
			pstmt.setInt(4, cdInconsistenciaOld!=0 ? cdInconsistenciaOld : objeto.getCdInconsistencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int delete(int cdInconsistencia) {
		return delete(cdInconsistencia, null);
	}

	public static int delete(int cdInconsistencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_inconsistencia WHERE cd_inconsistencia=?");
			pstmt.setInt(1, cdInconsistencia);

			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Inconsistencia get(int cdInconsistencia) {
		return get(cdInconsistencia, null);
	}

	public static Inconsistencia get(int cdInconsistencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_inconsistencia WHERE cd_inconsistencia=?");
			pstmt.setInt(1, cdInconsistencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Inconsistencia(rs.getInt("cd_inconsistencia"),
						rs.getString("nm_inconsistencia"),
						rs.getInt("tp_inconsistencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_inconsistencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Inconsistencia> getList() {
		return getList(null);
	}

	public static ArrayList<Inconsistencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Inconsistencia> list = new ArrayList<Inconsistencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Inconsistencia obj = InconsistenciaDAO.get(rsm.getInt("cd_inconsistencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InconsistenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_inconsistencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
