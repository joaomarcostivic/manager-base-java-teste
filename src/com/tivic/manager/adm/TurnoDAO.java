package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class TurnoDAO{

	public static int insert(Turno objeto) {
		return insert(objeto, null);
	}

	public static int insert(Turno objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_turno", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getNrOrdem()<=0)
				objeto.setNrOrdem(code);
			objeto.setCdTurno(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_turno (cd_turno,"+
			                                  "nm_turno,"+
			                                  "id_turno,"+
			                                  "hr_inicio_turno,"+
			                                  "hr_final_turno," +
			                                  "nr_ordem) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTurno());
			pstmt.setString(3,objeto.getIdTurno());
			if(objeto.getHrInicioTurno()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrInicioTurno().getTimeInMillis()));
			if(objeto.getHrFinalTurno()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrFinalTurno().getTimeInMillis()));
			pstmt.setInt(6, objeto.getNrOrdem());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Turno objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Turno objeto, int cdTurnoOld) {
		return update(objeto, cdTurnoOld, null);
	}

	public static int update(Turno objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Turno objeto, int cdTurnoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_turno SET cd_turno=?,"+
												      		   "nm_turno=?,"+
												      		   "id_turno=?,"+
												      		   "hr_inicio_turno=?,"+
												      		   "hr_final_turno=?," +
												      		   "nr_ordem=? WHERE cd_turno=?");
			pstmt.setInt(1,objeto.getCdTurno());
			pstmt.setString(2,objeto.getNmTurno());
			pstmt.setString(3,objeto.getIdTurno());
			if(objeto.getHrInicioTurno()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrInicioTurno().getTimeInMillis()));
			if(objeto.getHrFinalTurno()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrFinalTurno().getTimeInMillis()));
			pstmt.setInt(6,objeto.getNrOrdem());
			pstmt.setInt(7, cdTurnoOld!=0 ? cdTurnoOld : objeto.getCdTurno());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTurno) {
		return delete(cdTurno, null);
	}

	public static int delete(int cdTurno, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_turno WHERE cd_turno=?");
			pstmt.setInt(1, cdTurno);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Turno get(int cdTurno) {
		return get(cdTurno, null);
	}

	public static Turno get(int cdTurno, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_turno WHERE cd_turno=?");
			pstmt.setInt(1, cdTurno);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Turno(rs.getInt("cd_turno"),
						rs.getString("nm_turno"),
						rs.getString("id_turno"),
						(rs.getTimestamp("hr_inicio_turno")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_inicio_turno").getTime()),
						(rs.getTimestamp("hr_final_turno")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_final_turno").getTime()),
						rs.getInt("nr_ordem"));
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_turno ORDER BY hr_inicio_turno");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM adm_turno", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
