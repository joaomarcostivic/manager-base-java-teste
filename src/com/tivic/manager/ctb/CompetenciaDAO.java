package com.tivic.manager.ctb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class CompetenciaDAO{

	public static int insert(Competencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(Competencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_competencia");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_exercicio");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdExercicio()));
			int code = Conexao.getSequenceCode("ctb_competencia", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCompetencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_competencia (cd_competencia,"+
			                                  "cd_exercicio,"+
			                                  "nm_competencia,"+
			                                  "dt_inicial,"+
			                                  "dt_final) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdExercicio());
			pstmt.setString(3,objeto.getNmCompetencia());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Competencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Competencia objeto, int cdCompetenciaOld, int cdExercicioOld) {
		return update(objeto, cdCompetenciaOld, cdExercicioOld, null);
	}

	public static int update(Competencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Competencia objeto, int cdCompetenciaOld, int cdExercicioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_competencia SET cd_competencia=?,"+
												      		   "cd_exercicio=?,"+
												      		   "nm_competencia=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=? WHERE cd_competencia=? AND cd_exercicio=?");
			pstmt.setInt(1,objeto.getCdCompetencia());
			pstmt.setInt(2,objeto.getCdExercicio());
			pstmt.setString(3,objeto.getNmCompetencia());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(6, cdCompetenciaOld!=0 ? cdCompetenciaOld : objeto.getCdCompetencia());
			pstmt.setInt(7, cdExercicioOld!=0 ? cdExercicioOld : objeto.getCdExercicio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCompetencia, int cdExercicio) {
		return delete(cdCompetencia, cdExercicio, null);
	}

	public static int delete(int cdCompetencia, int cdExercicio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_competencia WHERE cd_competencia=? AND cd_exercicio=?");
			pstmt.setInt(1, cdCompetencia);
			pstmt.setInt(2, cdExercicio);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Competencia get(int cdCompetencia, int cdExercicio) {
		return get(cdCompetencia, cdExercicio, null);
	}

	public static Competencia get(int cdCompetencia, int cdExercicio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_competencia WHERE cd_competencia=? AND cd_exercicio=?");
			pstmt.setInt(1, cdCompetencia);
			pstmt.setInt(2, cdExercicio);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Competencia(rs.getInt("cd_competencia"),
						rs.getInt("cd_exercicio"),
						rs.getString("nm_competencia"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_competencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Competencia> getList() {
		return getList(null);
	}

	public static ArrayList<Competencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Competencia> list = new ArrayList<Competencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Competencia obj = CompetenciaDAO.get(rsm.getInt("cd_competencia"), rsm.getInt("cd_exercicio"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ctb_competencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
