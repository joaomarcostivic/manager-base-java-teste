package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class MatriculaUnidadeServices {

	public static Result save(MatriculaUnidade matriculaUnidade){
		return save(matriculaUnidade, null, null);
	}

	public static Result save(MatriculaUnidade matriculaUnidade, AuthData authData){
		return save(matriculaUnidade, authData, null);
	}

	public static Result save(MatriculaUnidade matriculaUnidade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(matriculaUnidade==null)
				return new Result(-1, "Erro ao salvar. MatriculaUnidade é nulo");

			int retorno;
			if(MatriculaUnidadeDAO.get(matriculaUnidade.getCdMatricula(), matriculaUnidade.getCdUnidade(), matriculaUnidade.getCdCurso())==null){
				retorno = MatriculaUnidadeDAO.insert(matriculaUnidade, connect);
				matriculaUnidade.setCdMatricula(retorno);
			}
			else {
				retorno = MatriculaUnidadeDAO.update(matriculaUnidade, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MATRICULAUNIDADE", matriculaUnidade);
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
	public static Result remove(MatriculaUnidade matriculaUnidade) {
		return remove(matriculaUnidade.getCdMatricula(), matriculaUnidade.getCdUnidade(), matriculaUnidade.getCdCurso());
	}
	public static Result remove(int cdMatricula, int cdUnidade, int cdCurso){
		return remove(cdMatricula, cdUnidade, cdCurso, false, null, null);
	}
	public static Result remove(int cdMatricula, int cdUnidade, int cdCurso, boolean cascade){
		return remove(cdMatricula, cdUnidade, cdCurso, cascade, null, null);
	}
	public static Result remove(int cdMatricula, int cdUnidade, int cdCurso, boolean cascade, AuthData authData){
		return remove(cdMatricula, cdUnidade, cdCurso, cascade, authData, null);
	}
	public static Result remove(int cdMatricula, int cdUnidade, int cdCurso, boolean cascade, AuthData authData, Connection connect){
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
			retorno = MatriculaUnidadeDAO.delete(cdMatricula, cdUnidade, cdCurso, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_unidade");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaUnidadeServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findParecer(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap findParecer(ArrayList<ItemComparator> criterios, Connection connect) {
		ResultSetMap rsm = Search.find("SELECT * FROM acd_matricula_unidade", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		while(rsm.next()){
			CursoUnidade cursoUnidade = CursoUnidadeDAO.get(rsm.getInt("cd_unidade"), rsm.getInt("cd_curso"), connect);
			rsm.setValueToField("NM_UNIDADE", cursoUnidade.getNmUnidade());
		}
		rsm.beforeFirst();
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("CD_UNIDADE");
		fields.add("DT_AVALIACAO");
		rsm.orderBy(fields);
		
		rsm.beforeFirst();
		return rsm;
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM acd_matricula_unidade", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}