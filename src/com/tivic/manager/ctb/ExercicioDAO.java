package com.tivic.manager.ctb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ExercicioDAO{

	public static int insert(Exercicio objeto) {
		return insert(objeto, null);
	}

	public static int insert(Exercicio objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_exercicio", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdExercicio(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_exercicio (cd_exercicio,"+
			                                  "nm_exercicio,"+
			                                  "dt_inicial,"+
			                                  "dt_final) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmExercicio());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Exercicio objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Exercicio objeto, int cdExercicioOld) {
		return update(objeto, cdExercicioOld, null);
	}

	public static int update(Exercicio objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Exercicio objeto, int cdExercicioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_exercicio SET cd_exercicio=?,"+
												      		   "nm_exercicio=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=? WHERE cd_exercicio=?");
			pstmt.setInt(1,objeto.getCdExercicio());
			pstmt.setString(2,objeto.getNmExercicio());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(5, cdExercicioOld!=0 ? cdExercicioOld : objeto.getCdExercicio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdExercicio) {
		return delete(cdExercicio, null);
	}

	public static int delete(int cdExercicio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_exercicio WHERE cd_exercicio=?");
			pstmt.setInt(1, cdExercicio);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Exercicio get(int cdExercicio) {
		return get(cdExercicio, null);
	}

	public static Exercicio get(int cdExercicio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_exercicio WHERE cd_exercicio=?");
			pstmt.setInt(1, cdExercicio);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Exercicio(rs.getInt("cd_exercicio"),
						rs.getString("nm_exercicio"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_exercicio");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Exercicio> getList() {
		return getList(null);
	}

	public static ArrayList<Exercicio> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Exercicio> list = new ArrayList<Exercicio>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Exercicio obj = ExercicioDAO.get(rsm.getInt("cd_exercicio"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExercicioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ctb_exercicio", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
