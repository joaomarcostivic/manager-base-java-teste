package com.tivic.manager.ctb;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CompetenciaServices {

	public static Result save(Competencia competencia){
		return save(competencia, null);
	}

	public static Result save(Competencia competencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(competencia==null)
				return new Result(-1, "Erro ao salvar. Competencia é nulo");

			int retorno;
			if(competencia.getCdCompetencia()==0){
				retorno = CompetenciaDAO.insert(competencia, connect);
				competencia.setCdCompetencia(retorno);
			}
			else {
				retorno = CompetenciaDAO.update(competencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "COMPETENCIA", competencia);
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
	public static Result remove(int cdCompetencia, int cdExercicio){
		return remove(cdCompetencia, cdExercicio, false, null);
	}
	public static Result remove(int cdCompetencia, int cdExercicio, boolean cascade){
		return remove(cdCompetencia, cdExercicio, cascade, null);
	}
	public static Result remove(int cdCompetencia, int cdExercicio, boolean cascade, Connection connect){
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
			retorno = CompetenciaDAO.delete(cdCompetencia, cdExercicio, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_competencia A"+
								" JOIN CTB_EXERCICIO B ON ( A.CD_EXERCICIO = B.CD_EXERCICIO ) ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CompetenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_competencia A"+
							" JOIN CTB_EXERCICIO B ON ( A.CD_EXERCICIO = B.CD_EXERCICIO ) ",
							criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
