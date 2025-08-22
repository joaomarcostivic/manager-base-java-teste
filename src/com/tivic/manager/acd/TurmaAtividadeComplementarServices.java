package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TurmaAtividadeComplementarServices {

	public static Result save(TurmaAtividadeComplementar turmaAtividadeComplementar){
		return save(turmaAtividadeComplementar, null);
	}

	public static Result save(TurmaAtividadeComplementar turmaAtividadeComplementar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(turmaAtividadeComplementar==null)
				return new Result(-1, "Erro ao salvar. TurmaAtividadeComplementar é nulo");

			int retorno;
			if(turmaAtividadeComplementar.getCdAtividadeComplementar()==0){
				retorno = TurmaAtividadeComplementarDAO.insert(turmaAtividadeComplementar, connect);
				turmaAtividadeComplementar.setCdAtividadeComplementar(retorno);
			}
			else {
				retorno = TurmaAtividadeComplementarDAO.update(turmaAtividadeComplementar, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TURMAATIVIDADECOMPLEMENTAR", turmaAtividadeComplementar);
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
	public static Result remove(int cdAtividadeComplementar, int cdTurma){
		return remove(cdAtividadeComplementar, cdTurma, false, null);
	}
	public static Result remove(int cdAtividadeComplementar, int cdTurma, boolean cascade){
		return remove(cdAtividadeComplementar, cdTurma, cascade, null);
	}
	public static Result remove(int cdAtividadeComplementar, int cdTurma, boolean cascade, Connection connect){
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
				retorno = TurmaAtividadeComplementarDAO.delete(cdAtividadeComplementar, cdTurma, connect);
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
	
	public static Result removeByTurma(int cdTurma) {
		return removeByTurma(cdTurma, null);
	}
	public static Result removeByTurma(int cdTurma, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAll();
			while (rsm.next()) {
				if (rsm.getInt("cd_turma") == cdTurma) {
					retorno = remove(rsm.getInt("cd_atividade_complementar"), cdTurma, true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
					}
				}
			}
			
			if (isConnectionNull)
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma_atividade_complementar");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_turma_atividade_complementar", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
