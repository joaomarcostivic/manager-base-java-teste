package com.tivic.manager.agd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class LocalHorarioBloqueadoDAO{

	public static int insert(LocalHorarioBloqueado objeto) {
		return insert(objeto, null);
	}

	public static int insert(LocalHorarioBloqueado objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agd_local_horario_bloqueado", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdHorarioBloqueado(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_local_horario_bloqueado (cd_horario_bloqueado,"+
			                                  "dt_inicio,"+
			                                  "dt_termino,"+
			                                  "hr_inicio,"+
			                                  "hr_termino,"+
			                                  "cd_local) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtInicio()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtTermino()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			if(objeto.getHrInicio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrInicio().getTimeInMillis()));
			if(objeto.getHrTermino()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrTermino().getTimeInMillis()));
			pstmt.setInt(6,objeto.getCdLocal());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LocalHorarioBloqueado objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LocalHorarioBloqueado objeto, int cdHorarioBloqueadoOld) {
		return update(objeto, cdHorarioBloqueadoOld, null);
	}

	public static int update(LocalHorarioBloqueado objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LocalHorarioBloqueado objeto, int cdHorarioBloqueadoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_local_horario_bloqueado SET cd_horario_bloqueado=?,"+
												      		   "dt_inicio=?,"+
												      		   "dt_termino=?,"+
												      		   "hr_inicio=?,"+
												      		   "hr_termino=?,"+
												      		   "cd_local=? WHERE cd_horario_bloqueado=?");
			pstmt.setInt(1,objeto.getCdHorarioBloqueado());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtTermino()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			if(objeto.getHrInicio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrInicio().getTimeInMillis()));
			if(objeto.getHrTermino()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrTermino().getTimeInMillis()));
			pstmt.setInt(6,objeto.getCdLocal());
			pstmt.setInt(7, cdHorarioBloqueadoOld!=0 ? cdHorarioBloqueadoOld : objeto.getCdHorarioBloqueado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdHorarioBloqueado) {
		return delete(cdHorarioBloqueado, null);
	}

	public static int delete(int cdHorarioBloqueado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_local_horario_bloqueado WHERE cd_horario_bloqueado=?");
			pstmt.setInt(1, cdHorarioBloqueado);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LocalHorarioBloqueado get(int cdHorarioBloqueado) {
		return get(cdHorarioBloqueado, null);
	}

	public static LocalHorarioBloqueado get(int cdHorarioBloqueado, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_local_horario_bloqueado WHERE cd_horario_bloqueado=?");
			pstmt.setInt(1, cdHorarioBloqueado);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LocalHorarioBloqueado(rs.getInt("cd_horario_bloqueado"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						(rs.getTimestamp("dt_termino")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino").getTime()),
						(rs.getTimestamp("hr_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_inicio").getTime()),
						(rs.getTimestamp("hr_termino")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_termino").getTime()),
						rs.getInt("cd_local"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_local_horario_bloqueado");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LocalHorarioBloqueado> getList() {
		return getList(null);
	}

	public static ArrayList<LocalHorarioBloqueado> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LocalHorarioBloqueado> list = new ArrayList<LocalHorarioBloqueado>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LocalHorarioBloqueado obj = LocalHorarioBloqueadoDAO.get(rsm.getInt("cd_horario_bloqueado"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM agd_local_horario_bloqueado", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
