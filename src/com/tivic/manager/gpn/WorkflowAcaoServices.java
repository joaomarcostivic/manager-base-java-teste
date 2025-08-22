package com.tivic.manager.gpn;

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

public class WorkflowAcaoServices {
	
	public static final int TP_ACAO_LANCAR_ANDAMENTO = 0;
	public static final int TP_ACAO_LANCAR_AGENDA = 1;
	public static final int TP_ACAO_MUDAR_FASE = 2;
	public static final int TP_ACAO_ENVIAR_EMAIL = 3;
	public static final int TP_ACAO_LANCAR_RECEITA = 4;
	public static final int TP_ACAO_LANCAR_DESPESA = 5;
	
	public static final int TP_RESPONSAVEL_ADV     = 0;
	public static final int TP_RESPONSAVEL_GRUPO   = 1;
	public static final int TP_RESPONSAVEL_USUARIO = 2;

	public static final int TP_CONTAGEM_DIAS_CORRIDOS = 0;
	public static final int TP_CONTAGEM_DIAS_UTEIS    = 1;

	public static Result save(WorkflowAcao workflowAcao){
		return save(workflowAcao, null, null);
	}

	public static Result save(WorkflowAcao workflowAcao, AuthData authData){
		return save(workflowAcao, authData, null);
	}

	public static Result save(WorkflowAcao workflowAcao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(workflowAcao==null)
				return new Result(-1, "Erro ao salvar. WorkflowAcao é nulo");

			int retorno;
			if(workflowAcao.getCdAcao()==0){
				retorno = WorkflowAcaoDAO.insert(workflowAcao, connect);
				workflowAcao.setCdAcao(retorno);
			}
			else {
				retorno = WorkflowAcaoDAO.update(workflowAcao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "WORKFLOWACAO", workflowAcao);
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
	
	public static Result remove(int cdRegra, AuthData auth){
		return remove(cdRegra, auth, null);
	}
	
	public static Result remove(int cdRegra, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			PreparedStatement ps = connect.prepareStatement("DELETE FROM gpn_workflow_acao WHERE cd_regra="+cdRegra);
			retorno = ps.executeUpdate();
			
			if(retorno<0){
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
	
	
	public static Result remove(int cdRegra) {
		return remove(cdRegra, (Connection)null);
	}
	
	public static Result remove(int cdRegra, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			PreparedStatement ps = connect.prepareStatement("DELETE FROM gpn_workflow_acao WHERE cd_regra="+cdRegra);
			retorno = ps.executeUpdate();
			
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
	
	public static Result remove(int cdAcao, int cdRegra){
		return remove(cdAcao, cdRegra, false, null, null);
	}
	public static Result remove(int cdAcao, int cdRegra, boolean cascade){
		return remove(cdAcao, cdRegra, cascade, null, null);
	}
	public static Result remove(int cdAcao, int cdRegra, boolean cascade, AuthData authData){
		return remove(cdAcao, cdRegra, cascade, authData, null);
	}
	public static Result remove(int cdAcao, int cdRegra, boolean cascade, AuthData authData, Connection connect){
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
			retorno = WorkflowAcaoDAO.delete(cdAcao, cdRegra, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM gpn_workflow_acao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoServices.getAll: " + e);
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
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = 
				    " SELECT A.*,"
				  + " 	B.nm_tipo_andamento, "
				  + " 	C.nm_tipo_prazo, C.tp_agenda_item,"
				  + " 	D.nm_tipo_situacao, "
				  + " 	E.nm_produto_servico,"
				  + " 	F.nm_pessoa,"
				  + " 	G.nm_grupo AS nm_grupo_trabalho, "
				  + " 	H.nm_tipo_documento "
				  + " FROM gpn_workflow_acao 			  A"
				  + " LEFT OUTER JOIN prc_tipo_andamento  B ON (A.cd_tipo_andamento = B.cd_tipo_andamento)"
				  + " LEFT OUTER JOIN prc_tipo_prazo 	  C ON (A.cd_tipo_prazo = C.cd_tipo_prazo)"
				  + " LEFT OUTER JOIN prc_tipo_situacao   D ON (A.cd_tipo_situacao = D.cd_tipo_situacao)"
				  + " LEFT OUTER JOIN grl_produto_servico E ON (A.cd_produto_servico = E.cd_produto_servico)"
				  + " LEFT OUTER JOIN grl_pessoa		  F ON (A.cd_pessoa = F.cd_pessoa) "
				  + " LEFT OUTER JOIN agd_grupo			  G ON (A.cd_grupo_trabalho = G.cd_grupo)"
				  + " LEFT OUTER JOIN gpn_tipo_documento  H ON (A.cd_tipo_documento = H.cd_tipo_documento)";
			
			return Search.find(sql, criterios, connect, connect==null);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoServices.find: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}