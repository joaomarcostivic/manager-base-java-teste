package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CursoUnidadeServices {

	public static Result save(CursoUnidade cursoUnidade){
		return save(cursoUnidade, null);
	}

	public static Result save(CursoUnidade cursoUnidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cursoUnidade==null)
				return new Result(-1, "Erro ao salvar. CursoUnidade é nulo");

			int retorno;
			if(cursoUnidade.getCdUnidade()==0){
				
				if (cursoUnidade.getNrOrdem() <= 0)
					cursoUnidade.setNrOrdem(CursoUnidadeDAO.getNextNrOrdem(cursoUnidade.getCdCurso()));
				
				retorno = CursoUnidadeDAO.insert(cursoUnidade, connect);
				
				cursoUnidade.setCdUnidade(retorno);
				
				// Gerar Avaliacões padrão
				// retorno = DisciplinaAvaliacaoServices.gerarAvaliacoesPadrao(cursoUnidade.getCdCurso(), connect).getCode();
			}
			else {
				retorno = CursoUnidadeDAO.update(cursoUnidade, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CURSOUNIDADE", cursoUnidade);
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
	public static Result remove(int cdUnidade, int cdCurso){
		return remove(cdUnidade, cdCurso, false, null);
	}
	public static Result remove(int cdUnidade, int cdCurso, boolean cascade){
		return remove(cdUnidade, cdCurso, cascade, null);
	}
	public static Result remove(int cdUnidade, int cdCurso, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				// Excluindo disciplina avaliacao relacionadas ao curso
				retorno = DisciplinaAvaliacaoServices.removeByUnidade(cdUnidade, connect).getCode();
			}
			if(!cascade || retorno>0)
				retorno = CursoUnidadeDAO.delete(cdUnidade, cdCurso, connect);
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
			ResultSetMap rsm = getAllByCurso(cdCurso);
			while (rsm.next()) {
				retorno = remove(rsm.getInt("cd_unidade"), cdCurso, true, connect).getCode();
				if(retorno<=0){
					Conexao.rollback(connect);
					return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_unidade");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_curso_unidade", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllByCurso( int cdCurso ) {
		return getAllByCurso( cdCurso, null );
	}
	
	public static ResultSetMap getAllByCurso( int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_unidade WHERE cd_curso = "+String.valueOf( cdCurso) + " ORDER BY nm_unidade");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
