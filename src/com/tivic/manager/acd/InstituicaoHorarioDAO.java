package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class InstituicaoHorarioDAO{

	public static int insert(InstituicaoHorario objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoHorario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_instituicao_horario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdHorario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_horario (cd_horario,"+
			                                  "tp_turno,"+
			                                  "hr_inicio,"+
			                                  "hr_termino,"+
			                                  "nr_dia_semana,"+
			                                  "cd_instituicao,"+
			                                  "cd_periodo_letivo) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getTpTurno());
			
			if(objeto.getHrInicio()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getHrInicio().getTimeInMillis()));
			
			
			if(objeto.getHrTermino()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrTermino().getTimeInMillis()));
			pstmt.setInt(5,objeto.getNrDiaSemana());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdInstituicao());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoHorario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(InstituicaoHorario objeto, int cdHorarioOld) {
		return update(objeto, cdHorarioOld, null);
	}

	public static int update(InstituicaoHorario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(InstituicaoHorario objeto, int cdHorarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_horario SET cd_horario=?,"+
												      		   "tp_turno=?,"+
												      		   "hr_inicio=?,"+
												      		   "hr_termino=?,"+
												      		   "nr_dia_semana=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_periodo_letivo=? WHERE cd_horario=?");
			pstmt.setInt(1,objeto.getCdHorario());
			pstmt.setInt(2,objeto.getTpTurno());
			if(objeto.getHrInicio()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getHrInicio().getTimeInMillis()));
			if(objeto.getHrTermino()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrTermino().getTimeInMillis()));
			pstmt.setInt(5,objeto.getNrDiaSemana());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdInstituicao());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPeriodoLetivo());
			pstmt.setInt(8, cdHorarioOld!=0 ? cdHorarioOld : objeto.getCdHorario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_horario WHERE cd_horario=?");
			pstmt.setInt(1, cdHorario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoHorario get(int cdHorario) {
		return get(cdHorario, null);
	}

	public static InstituicaoHorario get(int cdHorario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_horario WHERE cd_horario=?");
			pstmt.setInt(1, cdHorario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoHorario(rs.getInt("cd_horario"),
						rs.getInt("tp_turno"),
						(rs.getTimestamp("hr_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_inicio").getTime()),
						(rs.getTimestamp("hr_termino")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_termino").getTime()),
						rs.getInt("nr_dia_semana"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_periodo_letivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_horario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoHorario> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoHorario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoHorario> list = new ArrayList<InstituicaoHorario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoHorario obj = InstituicaoHorarioDAO.get(rsm.getInt("cd_horario"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_horario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}