package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class MatriculaModuloServices {

	public static Result save(MatriculaModulo matriculaModulo){
		return save(matriculaModulo, null);
	}

	public static Result save(MatriculaModulo matriculaModulo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(matriculaModulo==null)
				return new Result(-1, "Erro ao salvar. MatriculaModulo é nulo");

			int retorno;
			if(MatriculaModuloDAO.get(matriculaModulo.getCdMatricula(), matriculaModulo.getCdCursoModulo()) == null){
				retorno = MatriculaModuloDAO.insert(matriculaModulo, connect);
				matriculaModulo.setCdMatricula(retorno);
			}
			else {
				retorno = MatriculaModuloDAO.update(matriculaModulo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MATRICULAMODULO", matriculaModulo);
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
	
	public static Result remove(int cdMatricula, int cdCursoModulo){
		return remove(cdMatricula, cdCursoModulo, false, null);
	}
	
	public static Result remove(int cdMatricula, int cdCursoModulo, boolean cascade){
		return remove(cdMatricula, cdCursoModulo, cascade, null);
	}
	
	public static Result remove(int cdMatricula, int cdCursoModulo, boolean cascade, Connection connect){
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
			retorno = MatriculaModuloDAO.delete(cdMatricula, cdCursoModulo, connect);
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

	public static Result removeByMatricula(int cdMatricula) {
		return removeByMatricula(cdMatricula, null);
	}
	
	public static Result removeByMatricula(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAllByMatricula(cdMatricula, connect);
			while (rsm.next()) {
				if (rsm.getInt("cd_matricula") == cdMatricula) {
					retorno = remove(cdMatricula, rsm.getInt("cd_curso_modulo"), true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Esta matrícula está vinculada a outros registros e não pode ser excluída!");
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Matrícula excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir matrícula!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static MatriculaModulo get(MatriculaModulo matriculaModulo) {
		return get(matriculaModulo, null);
	}
	
	public static MatriculaModulo get(MatriculaModulo matriculaModulo, Connection connect) {
		return MatriculaModuloDAO.get(matriculaModulo.getCdMatricula(), matriculaModulo.getCdCursoModulo(), connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_modulo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByMatricula(int cdMatricula) {
		return getAllByMatricula(cdMatricula, null);
	}

	public static ResultSetMap getAllByMatricula(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_modulo WHERE cd_matricula = " + cdMatricula);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaModuloServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_modulo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getModulosByMatricula(int cdMatricula) {
		return getModulosByMatricula(cdMatricula, null);
	}

	public static ResultSetMap getModulosByMatricula(int cdMatricula, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, C.nm_produto_servico " +
					"FROM acd_matricula_modulo A, acd_curso_modulo B, grl_produto_servico C " +
					"WHERE A.cd_curso_modulo   = B.cd_curso_modulo " +
					"  AND B.cd_curso_modulo   = C.cd_produto_servico " +
					"  AND A.cd_matricula = " +cdMatricula);
			return new ResultSetMap(pstmt.executeQuery());
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
