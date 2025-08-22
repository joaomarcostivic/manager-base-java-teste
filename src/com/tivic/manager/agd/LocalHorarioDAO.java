package com.tivic.manager.agd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class LocalHorarioDAO{

	public static int insert(LocalHorario objeto) {
		return insert(objeto, null);
	}

	public static int insert(LocalHorario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agd_local_horario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdHorario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_local_horario (cd_horario,"+
			                                  "hr_inicio,"+
			                                  "hr_termino,"+
			                                  "nr_dia_semana,"+
			                                  "cd_local,"+
			                                  "st_horario) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getHrInicio()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getHrInicio().getTimeInMillis()));
			if(objeto.getHrTermino()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getHrTermino().getTimeInMillis()));
			pstmt.setInt(4,objeto.getNrDiaSemana());
			pstmt.setInt(5,objeto.getCdLocal());
			pstmt.setInt(6,objeto.getStHorario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LocalHorario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LocalHorario objeto, int cdHorarioOld) {
		return update(objeto, cdHorarioOld, null);
	}

	public static int update(LocalHorario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LocalHorario objeto, int cdHorarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_local_horario SET cd_horario=?,"+
												      		   "hr_inicio=?,"+
												      		   "hr_termino=?,"+
												      		   "nr_dia_semana=?,"+
												      		   "cd_local=?,"+
												      		   "st_horario=? WHERE cd_horario=?");
			pstmt.setInt(1,objeto.getCdHorario());
			if(objeto.getHrInicio()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getHrInicio().getTimeInMillis()));
			if(objeto.getHrTermino()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getHrTermino().getTimeInMillis()));
			pstmt.setInt(4,objeto.getNrDiaSemana());
			pstmt.setInt(5,objeto.getCdLocal());
			pstmt.setInt(6,objeto.getStHorario());
			pstmt.setInt(7, cdHorarioOld!=0 ? cdHorarioOld : objeto.getCdHorario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdHorario) {
		return delete(cdHorario, null);
	}

	public static int delete(int cdHorario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_local_horario WHERE cd_horario=?");
			pstmt.setInt(1, cdHorario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LocalHorario get(int cdHorario) {
		return get(cdHorario, null);
	}

	public static LocalHorario get(int cdHorario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_local_horario WHERE cd_horario=?");
			pstmt.setInt(1, cdHorario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LocalHorario(rs.getInt("cd_horario"),
						(rs.getTimestamp("hr_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_inicio").getTime()),
						(rs.getTimestamp("hr_termino")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_termino").getTime()),
						rs.getInt("nr_dia_semana"),
						rs.getInt("cd_local"),
						rs.getInt("st_horario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_local_horario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LocalHorario> getList() {
		return getList(null);
	}

	public static ArrayList<LocalHorario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LocalHorario> list = new ArrayList<LocalHorario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LocalHorario obj = LocalHorarioDAO.get(rsm.getInt("cd_horario"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM agd_local_horario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
