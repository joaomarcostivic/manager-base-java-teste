package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AgendaParticipanteDAO{

	public static int insert(AgendaParticipante objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgendaParticipante objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agenda_participante (cd_agenda,"+
			                                  "cd_participante,"+
			                                  "st_participante,"+
			                                  "tp_participante) VALUES (?, ?, ?, ?)");
			if(objeto.getCdAgenda()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAgenda());
			if(objeto.getCdParticipante()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdParticipante());
			pstmt.setInt(3,objeto.getStParticipante());
			pstmt.setInt(4,objeto.getTpParticipante());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgendaParticipante objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AgendaParticipante objeto, int cdAgendaOld, int cdParticipanteOld) {
		return update(objeto, cdAgendaOld, cdParticipanteOld, null);
	}

	public static int update(AgendaParticipante objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AgendaParticipante objeto, int cdAgendaOld, int cdParticipanteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agenda_participante SET cd_agenda=?,"+
												      		   "cd_participante=?,"+
												      		   "st_participante=?,"+
												      		   "tp_participante=? WHERE cd_agenda=? AND cd_participante=?");
			pstmt.setInt(1,objeto.getCdAgenda());
			pstmt.setInt(2,objeto.getCdParticipante());
			pstmt.setInt(3,objeto.getStParticipante());
			pstmt.setInt(4,objeto.getTpParticipante());
			pstmt.setInt(5, cdAgendaOld!=0 ? cdAgendaOld : objeto.getCdAgenda());
			pstmt.setInt(6, cdParticipanteOld!=0 ? cdParticipanteOld : objeto.getCdParticipante());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgenda, int cdParticipante) {
		return delete(cdAgenda, cdParticipante, null);
	}

	public static int delete(int cdAgenda, int cdParticipante, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agenda_participante WHERE cd_agenda=? AND cd_participante=?");
			pstmt.setInt(1, cdAgenda);
			pstmt.setInt(2, cdParticipante);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgendaParticipante get(int cdAgenda, int cdParticipante) {
		return get(cdAgenda, cdParticipante, null);
	}

	public static AgendaParticipante get(int cdAgenda, int cdParticipante, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_participante WHERE cd_agenda=? AND cd_participante=?");
			pstmt.setInt(1, cdAgenda);
			pstmt.setInt(2, cdParticipante);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgendaParticipante(rs.getInt("cd_agenda"),
						rs.getInt("cd_participante"),
						rs.getInt("st_participante"),
						rs.getInt("tp_participante"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_participante");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_agenda_participante", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
