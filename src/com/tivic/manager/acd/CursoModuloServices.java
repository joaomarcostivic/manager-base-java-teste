package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CursoModuloServices {

	public static Result save(CursoModulo cursoModulo){
		return save(cursoModulo, null);
	}

	public static Result save(CursoModulo cursoModulo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cursoModulo==null)
				return new Result(-1, "Erro ao salvar. CursoModulo é nulo");

			int retorno;
			if(cursoModulo.getCdCursoModulo()==0 && CursoModuloServices.getById(cursoModulo.getCdCurso()+"001", cursoModulo.getCdCurso(), connect) == null){
				retorno = CursoModuloDAO.insert(cursoModulo, connect);
				cursoModulo.setCdCursoModulo(retorno);
			}
			else {
				retorno = CursoModuloDAO.update(cursoModulo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CURSOMODULO", cursoModulo);
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
	public static Result remove(int cdCursoModulo){
		return remove(cdCursoModulo, false, null);
	}
	public static Result remove(int cdCursoModulo, boolean cascade){
		return remove(cdCursoModulo, cascade, null);
	}
	public static Result remove(int cdCursoModulo, boolean cascade, Connection connect){
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
			retorno = CursoModuloDAO.delete(cdCursoModulo, connect);
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
			ResultSetMap rsm = getAll();
			while (rsm.next()) {
				if (rsm.getInt("cd_curso") == cdCurso) {
					retorno = remove(rsm.getInt("cd_curso_modulo"), true, connect).getCode();
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

	public static CursoModulo getById(String idProdutoServico, int cdCurso){
		return getById(idProdutoServico, cdCurso, null);
	}
	
	public static CursoModulo getById(String idProdutoServico, int cdCurso, Connection connect){
		return CursoModuloDAO.getById(idProdutoServico, cdCurso, connect);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_produto_servico, B.id_produto_servico " +
			           "FROM acd_curso_modulo A, grl_produto_servico B " +
			           "WHERE A.cd_curso_modulo = B.cd_produto_servico ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByCurso(int cdCurso) {
		return getAll(cdCurso, null);
	}
	
	public static ResultSetMap getAll(int cdCurso, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_produto_servico, B.id_produto_servico " +
					"FROM acd_curso_modulo A, grl_produto_servico B " +
					"WHERE A.cd_curso_modulo = B.cd_produto_servico AND A.cd_curso = " + cdCurso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloServices.getAll: " + e);
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
		return Search.find("SELECT A.*, B.nm_produto_servico, B.id_produto_servico " +
		           "FROM acd_curso_modulo A, grl_produto_servico B " +
		           "WHERE A.cd_curso_modulo = B.cd_produto_servico ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
