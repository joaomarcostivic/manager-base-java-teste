package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tivic.sol.connection.Conexao;


public class AgendaParticipanteServices {

	/* tipos de participantes */
	public static final int TP_TODOS = -1;
	public static final int TP_COLABORADOR = 0;
	public static final int TP_CLIENTE = 1;
	public static final int TP_CONVIDADO = 2;
	
	public static final String[] tipos = {"Colaborador", 
		"Cliente",
		"Convidado"};
	
	/* situacao do partipante */
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;
	
	public static int insert(AgendaParticipante participante) {
		return insert(participante, null);
	}

	public static int insert(AgendaParticipante participante, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM agd_agenda_participante " +
					"WHERE cd_agenda = ? " +
					"  AND cd_participante = ?");
			pstmt.setInt(1, participante.getCdAgenda());
			pstmt.setInt(2, participante.getCdParticipante());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				AgendaParticipante part = AgendaParticipanteDAO.get(participante.getCdAgenda(), participante.getCdParticipante(), connection);
				part.setStParticipante(AgendaParticipanteServices.ST_ATIVO);
				if (AgendaParticipanteDAO.update(part, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			else if (AgendaParticipanteDAO.insert(participante, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int delete(int cdAgenda, int cdParticipante) {
		return delete(cdAgenda, cdParticipante, null);
	}

	public static int delete(int cdAgenda, int cdParticipante, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
		
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM agd_agendamento_participante " +
					"WHERE cd_agenda = ? " +
					"  AND cd_participante = ?");
			pstmt.setInt(1, cdAgenda);
			pstmt.setInt(2, cdParticipante);
			pstmt.execute();
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				AgendaParticipante participante = AgendaParticipanteDAO.get(cdAgenda, cdParticipante, connection);
				participante.setStParticipante(ST_INATIVO);
				if (AgendaParticipanteDAO.update(participante, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			else if (AgendaParticipanteDAO.delete(cdAgenda, cdParticipante, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaParticipanteDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
}
