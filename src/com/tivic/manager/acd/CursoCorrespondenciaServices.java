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

public class CursoCorrespondenciaServices {

	public static Result save(CursoCorrespondencia cursoCorrespondencia){
		return save(cursoCorrespondencia, null, null);
	}

	public static Result save(CursoCorrespondencia cursoCorrespondencia, AuthData authData){
		return save(cursoCorrespondencia, authData, null);
	}

	public static Result save(CursoCorrespondencia cursoCorrespondencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cursoCorrespondencia==null)
				return new Result(-1, "Erro ao salvar. CursoCorrespondencia é nulo");

			int retorno;
			if(cursoCorrespondencia.getCdCurso()==0){
				retorno = CursoCorrespondenciaDAO.insert(cursoCorrespondencia, connect);
				cursoCorrespondencia.setCdCurso(retorno);
			}
			else {
				retorno = CursoCorrespondenciaDAO.update(cursoCorrespondencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CURSOCORRESPONDENCIA", cursoCorrespondencia);
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
	public static Result remove(int cdCurso, int cdCursoCorrespondencia){
		return remove(cdCurso, cdCursoCorrespondencia, false, null, null);
	}
	public static Result remove(int cdCurso, int cdCursoCorrespondencia, boolean cascade){
		return remove(cdCurso, cdCursoCorrespondencia, cascade, null, null);
	}
	public static Result remove(int cdCurso, int cdCursoCorrespondencia, boolean cascade, AuthData authData){
		return remove(cdCurso, cdCursoCorrespondencia, cascade, authData, null);
	}
	public static Result remove(int cdCurso, int cdCursoCorrespondencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = CursoCorrespondenciaDAO.delete(cdCurso, cdCursoCorrespondencia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_correspondencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoCorrespondenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_curso_correspondencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}