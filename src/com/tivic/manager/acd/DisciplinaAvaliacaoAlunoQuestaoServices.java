package com.tivic.manager.acd;

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

public class DisciplinaAvaliacaoAlunoQuestaoServices {

	public static Result save(DisciplinaAvaliacaoAlunoQuestao disciplinaAvaliacaoAlunoQuestao){
		return save(disciplinaAvaliacaoAlunoQuestao, null, null);
	}

	public static Result save(DisciplinaAvaliacaoAlunoQuestao disciplinaAvaliacaoAlunoQuestao, AuthData authData){
		return save(disciplinaAvaliacaoAlunoQuestao, authData, null);
	}

	public static Result save(DisciplinaAvaliacaoAlunoQuestao disciplinaAvaliacaoAlunoQuestao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(disciplinaAvaliacaoAlunoQuestao==null)
				return new Result(-1, "Erro ao salvar. DisciplinaAvaliacaoAlunoQuestao é nulo");

			int retorno;
			if(disciplinaAvaliacaoAlunoQuestao.getCdMatriculaDisciplina()==0){
				retorno = DisciplinaAvaliacaoAlunoQuestaoDAO.insert(disciplinaAvaliacaoAlunoQuestao, connect);
				disciplinaAvaliacaoAlunoQuestao.setCdMatriculaDisciplina(retorno);
			}
			else {
				retorno = DisciplinaAvaliacaoAlunoQuestaoDAO.update(disciplinaAvaliacaoAlunoQuestao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DISCIPLINAAVALIACAOALUNOQUESTAO", disciplinaAvaliacaoAlunoQuestao);
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
	public static Result remove(DisciplinaAvaliacaoAlunoQuestao disciplinaAvaliacaoAlunoQuestao) {
		return remove(disciplinaAvaliacaoAlunoQuestao.getCdMatriculaDisciplina(), disciplinaAvaliacaoAlunoQuestao.getCdOfertaAvaliacaoQuestao(), disciplinaAvaliacaoAlunoQuestao.getCdOfertaAvaliacao(), disciplinaAvaliacaoAlunoQuestao.getCdOferta());
	}
	public static Result remove(int cdMatriculaDisciplina, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta){
		return remove(cdMatriculaDisciplina, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, false, null, null);
	}
	public static Result remove(int cdMatriculaDisciplina, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, boolean cascade){
		return remove(cdMatriculaDisciplina, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cascade, null, null);
	}
	public static Result remove(int cdMatriculaDisciplina, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, boolean cascade, AuthData authData){
		return remove(cdMatriculaDisciplina, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cascade, authData, null);
	}
	public static Result remove(int cdMatriculaDisciplina, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, boolean cascade, AuthData authData, Connection connect){
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
			retorno = DisciplinaAvaliacaoAlunoQuestaoDAO.delete(cdMatriculaDisciplina, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_avaliacao_aluno_questao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoQuestaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_disciplina_avaliacao_aluno_questao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}