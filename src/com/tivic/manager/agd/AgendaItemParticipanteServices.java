package com.tivic.manager.agd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AgendaItemParticipanteServices {
	
	public static final int ST_ABERTO = 0;
	public static final int ST_CONFIRMADO = 1;
	

	public static Result save(AgendaItemParticipante agendaItemParticipante){
		return save(agendaItemParticipante, null);
	}

	public static Result save(AgendaItemParticipante agendaItemParticipante, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(agendaItemParticipante==null)
				return new Result(-1, "Erro ao salvar. AgendaItemParticipante é nulo");

			int retorno;
			if(AgendaItemParticipanteDAO.get(agendaItemParticipante.getCdAgendaItem(), agendaItemParticipante.getCdPessoa(), connect)==null) {//(agendaItemParticipante.getCdAgendaItem()==0){
				retorno = AgendaItemParticipanteDAO.insert(agendaItemParticipante, connect);
				agendaItemParticipante.setCdAgendaItem(retorno);
			}
			else {
				retorno = AgendaItemParticipanteDAO.update(agendaItemParticipante, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AGENDAITEMPARTICIPANTE", agendaItemParticipante);
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
	public static Result remove(int cdAgendaItem, int cdPessoa){
		return remove(cdAgendaItem, cdPessoa, false, null);
	}
	public static Result remove(int cdAgendaItem, int cdPessoa, boolean cascade){
		return remove(cdAgendaItem, cdPessoa, cascade, null);
	}
	public static Result remove(int cdAgendaItem, int cdPessoa, boolean cascade, Connection connect){
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
			retorno = AgendaItemParticipanteDAO.delete(cdAgendaItem, cdPessoa, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_item_participante");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteServices.getAll: " + e);
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
		return Search.find("SELECT A.*, B.nm_pessoa, B.nm_email "
						 + " FROM agd_agenda_item_participante A"
						 + " LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)", 
						 criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
