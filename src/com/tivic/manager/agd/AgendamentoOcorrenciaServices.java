package com.tivic.manager.agd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class AgendamentoOcorrenciaServices {

	public static Result save(AgendamentoOcorrencia agendamentoOcorrencia){
		return save(agendamentoOcorrencia, null, null);
	}

	public static Result save(AgendamentoOcorrencia agendamentoOcorrencia, AuthData authData){
		return save(agendamentoOcorrencia, authData, null);
	}

	public static Result save(AgendamentoOcorrencia agendamentoOcorrencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(agendamentoOcorrencia==null)
				return new Result(-1, "Erro ao salvar. AgendamentoOcorrencia é nulo");

			int retorno = 0;
			if(AgendamentoOcorrenciaDAO.get(agendamentoOcorrencia.getCdTipoOcorrencia(), agendamentoOcorrencia.getCdAgendamento())==null){
				retorno = AgendamentoOcorrenciaDAO.insert(agendamentoOcorrencia, connect);
				agendamentoOcorrencia.setCdTipoOcorrencia(retorno);
			}
			else {
//				retorno = AgendamentoOcorrenciaDAO.update(agendamentoOcorrencia, connect);
				TipoOcorrencia tipo = TipoOcorrenciaDAO.get(agendamentoOcorrencia.getCdTipoOcorrencia(), connect);
				return new Result(-2, "Já existe uma ocorrência de "+tipo.getNmTipoOcorrencia()+".");
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AGENDAMENTOOCORRENCIA", agendamentoOcorrencia);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(AgendamentoOcorrencia agendamentoOcorrencia) {
		return remove(agendamentoOcorrencia.getCdTipoOcorrencia(), agendamentoOcorrencia.getCdAgendamento());
	}
	public static Result remove(int cdTipoOcorrencia, int cdAgendamento){
		return remove(cdTipoOcorrencia, cdAgendamento, false, null, null);
	}
	public static Result remove(int cdTipoOcorrencia, int cdAgendamento, boolean cascade){
		return remove(cdTipoOcorrencia, cdAgendamento, cascade, null, null);
	}
	public static Result remove(int cdTipoOcorrencia, int cdAgendamento, boolean cascade, AuthData authData){
		return remove(cdTipoOcorrencia, cdAgendamento, cascade, authData, null);
	}
	public static Result remove(int cdTipoOcorrencia, int cdAgendamento, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AgendamentoOcorrenciaDAO.delete(cdTipoOcorrencia, cdAgendamento, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaServices.getAll: " + e);
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
			  + " C1.nm_tipo_ocorrencia, "
			  + " E.nm_pessoa "
			  + " FROM agd_agendamento_ocorrencia A"
			  + " JOIN agd_agendamento B ON (A.cd_agendamento = B.cd_agendamento)"
			  + " LEFT OUTER JOIN agd_tipo_ocorrencia C ON (A.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia)"
			  + " LEFT OUTER JOIN grl_tipo_ocorrencia C1 ON (C.cd_tipo_ocorrencia = C1.cd_tipo_ocorrencia)"
			  + " LEFT OUTER JOIN seg_usuario D ON (A.cd_usuario = D.cd_usuario)"
			  + " LEFT OUTER JOIN grl_pessoa E ON (D.cd_pessoa = E.cd_pessoa)", 
				" ORDER BY A.dt_ocorrencia DESC, A.dt_cadastro DESC ", 
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
