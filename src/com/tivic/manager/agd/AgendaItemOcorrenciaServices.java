package com.tivic.manager.agd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AgendaItemOcorrenciaServices {
	
	public static final int TP_PUBLICO = 0;
	public static final int TP_PRIVADO = 1;

	public static Result save(AgendaItemOcorrencia agendaItemOcorrencia){
		return save(agendaItemOcorrencia, null);
	}

	public static Result save(AgendaItemOcorrencia agendaItemOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(agendaItemOcorrencia==null)
				return new Result(-1, "Erro ao salvar. AgendaItemOcorrencia é nulo");

			int retorno;
			
			if(agendaItemOcorrencia.getCdOcorrencia()==0){
				retorno = AgendaItemOcorrenciaDAO.insert(agendaItemOcorrencia, connect);
				agendaItemOcorrencia.setCdOcorrencia(retorno);
			}
			else {
				retorno = AgendaItemOcorrenciaDAO.update(agendaItemOcorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AGENDAITEMOCORRENCIA", agendaItemOcorrencia);
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

	public static Result remove(AgendaItemOcorrencia ocorrencia){
		return remove(ocorrencia.getCdOcorrencia(), ocorrencia.getCdAgendaItem(), false, null);
	}
	public static Result remove(int cdOcorrencia, int cdAgendaItem){
		return remove(cdOcorrencia, cdAgendaItem, false, null);
	}
	public static Result remove(int cdOcorrencia, int cdAgendaItem, boolean cascade){
		return remove(cdOcorrencia, cdAgendaItem, cascade, null);
	}
	public static Result remove(int cdOcorrencia, int cdAgendaItem, boolean cascade, Connection connect){
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
				retorno = AgendaItemOcorrenciaDAO.delete(cdOcorrencia, cdAgendaItem, connect);
			
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_item_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByAgenda(int cdAgendaItem) {
		return getAllByAgenda(cdAgendaItem, null);
	}
	
	public static ResultSetMap getAllByAgenda(int cdAgendaItem, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql =
				" SELECT A.*, B.nm_tipo_ocorrencia, C.nm_login " +
				" FROM agd_agenda_item_ocorrencia A " +
				" JOIN grl_tipo_ocorrencia B ON (A.cd_tipo_ocorrencia = B.cd_tipo_ocorrencia)" +
				" LEFT OUTER JOIN seg_usuario C ON (A.cd_usuario = C.cd_usuario)" +
				" WHERE A.cd_agenda_item = "+cdAgendaItem+ 
				" ORDER BY A.dt_ocorrencia DESC";
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM agd_agenda_item_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
