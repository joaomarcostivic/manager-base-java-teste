package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CursoAreaInteresseServices {
	public static Result save(CursoAreaInteresse cursoAreaInteresse){
		return save(cursoAreaInteresse, null);
	}
	
	public static Result save(CursoAreaInteresse cursoAreaInteresse, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cursoAreaInteresse==null)
				return new Result(-1, "Erro ao salvar. Curso área de interesse é nulo");
			
			int retorno;
			if(CursoAreaInteresseDAO.get(cursoAreaInteresse.getCdAreaInteresse(), cursoAreaInteresse.getCdCurso())==null){
				retorno = CursoAreaInteresseDAO.insert(cursoAreaInteresse, connect);
				cursoAreaInteresse.setCdAreaInteresse(retorno);
			}
			else {
				retorno = CursoAreaInteresseDAO.update(cursoAreaInteresse, connect);
			}
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CURSOAREAINTERESSE", cursoAreaInteresse);
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
	
	public static Result remove(int cdCursoAreaInteresse, int cdCurso){
		return remove(cdCursoAreaInteresse, cdCurso, false, null);
	}
	
	public static Result remove(int cdCursoAreaInteresse, int cdCurso, boolean cascade){
		return remove(cdCursoAreaInteresse, cdCurso, cascade, null);
	}
	
	public static Result remove(int cdCursoAreaInteresse, int cdCurso, boolean cascade, Connection connect){
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
				retorno = CursoAreaInteresseDAO.delete(cdCursoAreaInteresse, cdCurso, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este curso área de interesse está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Curso area de interesse excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir curso área de interesse!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeByCurso(int cdCurso) {
		return removeByCurso(cdCurso, null);
	}
	
	public static Result removeByCurso(int cdCurso, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAll();
			while (rsm.next()) {
				if (rsm.getInt("cd_curso") == cdCurso) {
					retorno = remove(rsm.getInt("cd_area_interesse"), cdCurso, true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Este curso área de interesse está vinculado a outros registros e não pode ser excluído!");
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Curso area de interesse excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir curso área de interesse!");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_area_interesse ORDER BY cd_area_interesse");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoAreaInteresseDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_curso_area_interesse", "ORDER BY cd_area_interesse", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
