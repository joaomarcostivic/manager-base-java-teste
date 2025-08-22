package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CursoMultiServices {

	public static Result save(CursoMulti cursoMulti){
		return save(cursoMulti, null);
	}

	public static Result save(CursoMulti cursoMulti, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cursoMulti==null)
				return new Result(-1, "Erro ao salvar. CursoMulti é nulo");

			int retorno;
			if(cursoMulti.getCdCursoMulti()==0){
				retorno = CursoMultiDAO.insert(cursoMulti, connect);
				cursoMulti.setCdCursoMulti(retorno);
			}
			else {
				retorno = CursoMultiDAO.update(cursoMulti, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CURSOMULTI", cursoMulti);
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
	public static Result remove(int cdCursoMulti, int cdCurso){
		return remove(cdCursoMulti, cdCurso, false, null);
	}
	public static Result remove(int cdCursoMulti, int cdCurso, boolean cascade){
		return remove(cdCursoMulti, cdCurso, cascade, null);
	}
	public static Result remove(int cdCursoMulti, int cdCurso, boolean cascade, Connection connect){
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
			retorno = CursoMultiDAO.delete(cdCursoMulti, cdCurso, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_multi");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoMultiServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_curso_multi", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result addCurso(int cdCursoMulti, int cdCurso){
		return addCurso(cdCursoMulti, cdCurso, null);
	}

	public static Result addCurso(int cdCursoMulti, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			/*
			 * Verifica se o curso ja foi adicionado 
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_curso_multi " +
					                                "WHERE cd_curso_multi = "+cdCursoMulti+" AND cd_curso = "+cdCurso).executeQuery();
			/*
			 * Inclui o curso
			 */
			if(!rs.next())	{
				return new Result(connect.prepareStatement("INSERT INTO acd_curso_multi (cd_curso_multi,cd_curso) " +
                                                           "VALUES ("+cdCursoMulti+" ,"+cdCurso+")").executeUpdate());
			}
			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o curso no multi!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllCursosOf(int cdCursoMulti) {
		return getAllCursosOf(cdCursoMulti, null);
	}
	
	public static ResultSetMap getAllCursosOf(int cdCursoMulti, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_produto_servico AS nm_curso " +
					"FROM acd_curso A " +
					"JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) " +
					"JOIN acd_curso_multi C ON (A.cd_curso = C.cd_curso) " +
					"WHERE C.cd_curso_multi = ? ");
			
			pstmt.setInt(1, cdCursoMulti);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	

}
