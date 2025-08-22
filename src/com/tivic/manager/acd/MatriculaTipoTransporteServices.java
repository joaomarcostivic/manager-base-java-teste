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

public class MatriculaTipoTransporteServices {

	public static Result save(MatriculaTipoTransporte matriculaTipoTransporte){
		return save(matriculaTipoTransporte, null, null);
	}

	public static Result save(MatriculaTipoTransporte matriculaTipoTransporte, AuthData authData){
		return save(matriculaTipoTransporte, authData, null);
	}

	public static Result save(MatriculaTipoTransporte matriculaTipoTransporte, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(matriculaTipoTransporte==null)
				return new Result(-1, "Erro ao salvar. MatriculaTipoTransporte é nulo");

			int retorno;
			if(MatriculaTipoTransporteDAO.get(matriculaTipoTransporte.getCdMatricula(), matriculaTipoTransporte.getCdTipoTransporte(), connect)==null){
				retorno = MatriculaTipoTransporteDAO.insert(matriculaTipoTransporte, connect);
				matriculaTipoTransporte.setCdMatricula(retorno);
			}
			else {
				retorno = MatriculaTipoTransporteDAO.update(matriculaTipoTransporte, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MATRICULATIPOTRANSPORTE", matriculaTipoTransporte);
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
	
	public static Result saveTipoTransporte(int cdMatricula, int cdTipoTransporte){
		return saveTipoTransporte(cdMatricula, cdTipoTransporte, null);
	}

	public static Result saveTipoTransporte(int cdMatricula, int cdTipoTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 1;
			
			if(MatriculaTipoTransporteDAO.get(cdMatricula, cdTipoTransporte, connect) == null){
				retorno = MatriculaTipoTransporteDAO.insert(new MatriculaTipoTransporte(cdMatricula, cdTipoTransporte), connect);
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...");
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
	
	
	public static Result remove(int cdMatricula, int cdTipoTransporte){
		return remove(cdMatricula, cdTipoTransporte, false, null);
	}
	public static Result remove(int cdMatricula, int cdTipoTransporte, boolean cascade){
		return remove(cdMatricula, cdTipoTransporte, cascade, null);
	}
	public static Result remove(int cdMatricula, int cdTipoTransporte, boolean cascade, Connection connect){
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
			retorno = MatriculaTipoTransporteDAO.delete(cdMatricula, cdTipoTransporte, connect);
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

	public static ResultSetMap getTipoTransporteByMatricula(int cdMatricula) {
		return getTipoTransporteByMatricula(cdMatricula, null);
	}
	
	public static ResultSetMap getTipoTransporteByMatricula(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_tipo_transporte A, fta_tipo_transporte B "
					+ " WHERE A.cd_tipo_transporte = B.cd_tipo_transporte "
					+ "   AND A.cd_matricula = ? ");
			pstmt.setInt(1, cdMatricula);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteServices.getTipoTransporteByMatricula: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteServices.getTipoTransporteByMatricula: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_tipo_transporte");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_tipo_transporte A, fta_tipo_transporte B WHERE A.cd_tipo_transporte = B.cd_tipo_transporte", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}