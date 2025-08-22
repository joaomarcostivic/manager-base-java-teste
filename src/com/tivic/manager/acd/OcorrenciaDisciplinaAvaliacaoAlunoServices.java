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

public class OcorrenciaDisciplinaAvaliacaoAlunoServices {

	public static Result save(OcorrenciaDisciplinaAvaliacaoAluno ocorrenciaDisciplinaAvaliacaoAluno){
		return save(ocorrenciaDisciplinaAvaliacaoAluno, null, null);
	}

	public static Result save(OcorrenciaDisciplinaAvaliacaoAluno ocorrenciaDisciplinaAvaliacaoAluno, AuthData authData){
		return save(ocorrenciaDisciplinaAvaliacaoAluno, authData, null);
	}

	public static Result save(OcorrenciaDisciplinaAvaliacaoAluno ocorrenciaDisciplinaAvaliacaoAluno, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ocorrenciaDisciplinaAvaliacaoAluno==null)
				return new Result(-1, "Erro ao salvar. OcorrenciaDisciplinaAvaliacaoAluno é nulo");

			int retorno;
			if(ocorrenciaDisciplinaAvaliacaoAluno.getCdOcorrencia()==0){
				retorno = OcorrenciaDisciplinaAvaliacaoAlunoDAO.insert(ocorrenciaDisciplinaAvaliacaoAluno, connect);
				ocorrenciaDisciplinaAvaliacaoAluno.setCdOcorrencia(retorno);
			}
			else {
				retorno = OcorrenciaDisciplinaAvaliacaoAlunoDAO.update(ocorrenciaDisciplinaAvaliacaoAluno, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OCORRENCIADISCIPLINAAVALIACAOALUNO", ocorrenciaDisciplinaAvaliacaoAluno);
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
	public static Result remove(OcorrenciaDisciplinaAvaliacaoAluno ocorrenciaDisciplinaAvaliacaoAluno) {
		return remove(ocorrenciaDisciplinaAvaliacaoAluno.getCdOcorrencia());
	}
	public static Result remove(int cdOcorrencia){
		return remove(cdOcorrencia, false, null, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade){
		return remove(cdOcorrencia, cascade, null, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade, AuthData authData){
		return remove(cdOcorrencia, cascade, authData, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OcorrenciaDisciplinaAvaliacaoAlunoDAO.delete(cdOcorrencia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_disciplina_avaliacao_aluno");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDisciplinaAvaliacaoAlunoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_ocorrencia_disciplina_avaliacao_aluno", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}