package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AgendamentoParticipanteDAO{

	public static int insert(AgendamentoParticipante objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgendamentoParticipante objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agendamento_participante (cd_agendamento,"+
			                                  "cd_agenda,"+
			                                  "cd_participante,"+
			                                  "lg_presenca) VALUES (?, ?, ?, ?)");
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAgendamento());
			if(objeto.getCdAgenda()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgenda());
			if(objeto.getCdParticipante()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdParticipante());
			pstmt.setInt(4,objeto.getLgPresenca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgendamentoParticipante objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(AgendamentoParticipante objeto, int cdAgendamentoOld, int cdAgendaOld, int cdParticipanteOld) {
		return update(objeto, cdAgendamentoOld, cdAgendaOld, cdParticipanteOld, null);
	}

	public static int update(AgendamentoParticipante objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(AgendamentoParticipante objeto, int cdAgendamentoOld, int cdAgendaOld, int cdParticipanteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agendamento_participante SET cd_agendamento=?,"+
												      		   "cd_agenda=?,"+
												      		   "cd_participante=?,"+
												      		   "lg_presenca=? WHERE cd_agendamento=? AND cd_agenda=? AND cd_participante=?");
			pstmt.setInt(1,objeto.getCdAgendamento());
			pstmt.setInt(2,objeto.getCdAgenda());
			pstmt.setInt(3,objeto.getCdParticipante());
			pstmt.setInt(4,objeto.getLgPresenca());
			pstmt.setInt(5, cdAgendamentoOld!=0 ? cdAgendamentoOld : objeto.getCdAgendamento());
			pstmt.setInt(6, cdAgendaOld!=0 ? cdAgendaOld : objeto.getCdAgenda());
			pstmt.setInt(7, cdParticipanteOld!=0 ? cdParticipanteOld : objeto.getCdParticipante());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgendamento, int cdAgenda, int cdParticipante) {
		return delete(cdAgendamento, cdAgenda, cdParticipante, null);
	}

	public static int delete(int cdAgendamento, int cdAgenda, int cdParticipante, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agendamento_participante WHERE cd_agendamento=? AND cd_agenda=? AND cd_participante=?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.setInt(2, cdAgenda);
			pstmt.setInt(3, cdParticipante);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgendamentoParticipante get(int cdAgendamento, int cdAgenda, int cdParticipante) {
		return get(cdAgendamento, cdAgenda, cdParticipante, null);
	}

	public static AgendamentoParticipante get(int cdAgendamento, int cdAgenda, int cdParticipante, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_participante WHERE cd_agendamento=? AND cd_agenda=? AND cd_participante=?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.setInt(2, cdAgenda);
			pstmt.setInt(3, cdParticipante);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgendamentoParticipante(rs.getInt("cd_agendamento"),
						rs.getInt("cd_agenda"),
						rs.getInt("cd_participante"),
						rs.getInt("lg_presenca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_participante");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_agendamento_participante", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
