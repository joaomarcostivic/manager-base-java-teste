package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AgendamentoParticipanteServices {

	/* tipos de participantes */
	public static final int TP_TODOS = -1;
	public static final int TP_COLABORADOR = 0;
	public static final int TP_CLIENTE = 1;
	public static final int TP_CONVIDADO = 2;
	
	public static final String[] tipos = {"Colaborador", 
		"Cliente",
		"Convidado"};
	
	public static Result save(AgendamentoParticipante agendamentoParticipante){
		return save(agendamentoParticipante, null, null);
	}

	public static Result save(AgendamentoParticipante agendamentoParticipante, AuthData authData){
		return save(agendamentoParticipante, authData, null);
	}

	public static Result save(AgendamentoParticipante agendamentoParticipante, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(agendamentoParticipante==null)
				return new Result(-1, "Erro ao salvar. AgendamentoParticipante é nulo");

			int retorno;
			if(AgendamentoParticipanteDAO.get(agendamentoParticipante.getCdAgendamento(), agendamentoParticipante.getCdAgenda(), agendamentoParticipante.getCdParticipante(), connect)==null){
				retorno = AgendamentoParticipanteDAO.insert(agendamentoParticipante, connect);
				agendamentoParticipante.setCdAgendamento(retorno);
			}
			else {
				retorno = AgendamentoParticipanteDAO.update(agendamentoParticipante, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AGENDAMENTOPARTICIPANTE", agendamentoParticipante);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(AgendamentoParticipante agendamentoParticipante) {
		return remove(agendamentoParticipante.getCdAgendamento(), agendamentoParticipante.getCdAgenda(), agendamentoParticipante.getCdParticipante());
	}
	public static Result remove(int cdAgendamento, int cdAgenda, int cdParticipante){
		return remove(cdAgendamento, cdAgenda, cdParticipante, false, null, null);
	}
	public static Result remove(int cdAgendamento, int cdAgenda, int cdParticipante, boolean cascade){
		return remove(cdAgendamento, cdAgenda, cdParticipante, cascade, null, null);
	}
	public static Result remove(int cdAgendamento, int cdAgenda, int cdParticipante, boolean cascade, AuthData authData){
		return remove(cdAgendamento, cdAgenda, cdParticipante, cascade, authData, null);
	}
	public static Result remove(int cdAgendamento, int cdAgenda, int cdParticipante, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = AgendamentoParticipanteDAO.delete(cdAgendamento, cdAgenda, cdParticipante, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			System.err.println("Erro! AgendamentoParticipanteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoParticipanteServices.getAll: " + e);
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
		return Search.find(
				" SELECT A.*, "
				+ " B.cd_pessoa, B.nm_pessoa, "
				+ " D.nm_grupo "
				+ " FROM agd_agendamento_participante A"
				+ " JOIN grl_pessoa B ON (A.cd_participante = B.cd_pessoa)"
				+ " LEFT OUTER JOIN grl_grupo_pessoa C ON (B.cd_pessoa = C.cd_pessoa)"
				+ " LEFT OUTER JOIN grl_grupo D ON (C.cd_grupo = D.cd_grupo)", 
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
}
