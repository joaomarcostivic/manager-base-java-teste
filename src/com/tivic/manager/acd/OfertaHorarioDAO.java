package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class OfertaHorarioDAO{

	public static int insert(OfertaHorario objeto) {
		return insert(objeto, null);
	}

	public static int insert(OfertaHorario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_horario");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_oferta");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdOferta()));
			int code = Conexao.getSequenceCode("acd_oferta_horario", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdHorario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_oferta_horario (cd_horario,"+
			                                  "cd_oferta,"+
			                                  "nr_dia_semana,"+
			                                  "hr_inicio,"+
			                                  "hr_termino,"+
			                                  "lg_semanal,"+
			                                  "st_horario,"+
			                                  "cd_horario_instituicao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			
			if(objeto.getCdOferta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOferta());
			pstmt.setInt(3,objeto.getNrDiaSemana());
			if(objeto.getHrInicio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrInicio().getTimeInMillis()));
			if(objeto.getHrTermino()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrTermino().getTimeInMillis()));
			pstmt.setInt(6,objeto.getLgSemanal());
			pstmt.setInt(7,objeto.getStHorario());
			if(objeto.getCdHorarioInstituicao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdHorarioInstituicao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OfertaHorario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(OfertaHorario objeto, int cdHorarioOld, int cdOfertaOld) {
		return update(objeto, cdHorarioOld, cdOfertaOld, null);
	}

	public static int update(OfertaHorario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(OfertaHorario objeto, int cdHorarioOld, int cdOfertaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_oferta_horario SET cd_horario=?,"+
												      		   "cd_oferta=?,"+
												      		   "nr_dia_semana=?,"+
												      		   "hr_inicio=?,"+
												      		   "hr_termino=?,"+
												      		   "lg_semanal=?,"+
												      		   "st_horario=?,"+
												      		   "cd_horario_instituicao=? WHERE cd_horario=? AND cd_oferta=?");
			pstmt.setInt(1,objeto.getCdHorario());
			pstmt.setInt(2,objeto.getCdOferta());
			pstmt.setInt(3,objeto.getNrDiaSemana());
			if(objeto.getHrInicio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrInicio().getTimeInMillis()));
			if(objeto.getHrTermino()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrTermino().getTimeInMillis()));
			pstmt.setInt(6,objeto.getLgSemanal());
			pstmt.setInt(7,objeto.getStHorario());
			if(objeto.getCdHorarioInstituicao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdHorarioInstituicao());
			pstmt.setInt(9, cdHorarioOld!=0 ? cdHorarioOld : objeto.getCdHorario());
			pstmt.setInt(10, cdOfertaOld!=0 ? cdOfertaOld : objeto.getCdOferta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdHorario, int cdOferta) {
		return delete(cdHorario, cdOferta, null);
	}

	public static int delete(int cdHorario, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_oferta_horario WHERE cd_horario=? AND cd_oferta=?");
			pstmt.setInt(1, cdHorario);
			pstmt.setInt(2, cdOferta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OfertaHorario get(int cdHorario, int cdOferta) {
		return get(cdHorario, cdOferta, null);
	}

	public static OfertaHorario get(int cdHorario, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_horario WHERE cd_horario=? AND cd_oferta=?");
			pstmt.setInt(1, cdHorario);
			pstmt.setInt(2, cdOferta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OfertaHorario(rs.getInt("cd_horario"),
						rs.getInt("cd_oferta"),
						rs.getInt("nr_dia_semana"),
						(rs.getTimestamp("hr_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_inicio").getTime()),
						(rs.getTimestamp("hr_termino")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_termino").getTime()),
						rs.getInt("lg_semanal"),
						rs.getInt("st_horario"),
						rs.getInt("cd_horario_instituicao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_horario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OfertaHorario> getList() {
		return getList(null);
	}

	public static ArrayList<OfertaHorario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OfertaHorario> list = new ArrayList<OfertaHorario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OfertaHorario obj = OfertaHorarioDAO.get(rsm.getInt("cd_horario"), rsm.getInt("cd_oferta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_oferta_horario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
