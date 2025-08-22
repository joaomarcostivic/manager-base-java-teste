package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class InstituicaoIndicadorServices {

	public static Result save(InstituicaoIndicador instituicaoIndicador){
		return save(instituicaoIndicador, null, null);
	}

	public static Result save(InstituicaoIndicador instituicaoIndicador, AuthData authData){
		return save(instituicaoIndicador, authData, null);
	}

	public static Result save(InstituicaoIndicador instituicaoIndicador, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoIndicador==null)
				return new Result(-1, "Erro ao salvar. InstituicaoIndicador é nulo");

			int retorno;
			retorno = InstituicaoIndicadorDAO.insert(instituicaoIndicador, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOINDICADOR", instituicaoIndicador);
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
	public static Result remove(int cdInstituicao, int cdIndicador, int cdPeriodoLetivo){
		return remove(cdInstituicao, cdIndicador, cdPeriodoLetivo, false, null);
	}
	public static Result remove(int cdInstituicao, int cdIndicador, int cdPeriodoLetivo, boolean cascade){
		return remove(cdInstituicao, cdIndicador, cdPeriodoLetivo, cascade, null);
	}
	public static Result remove(int cdInstituicao, int cdIndicador, int cdPeriodoLetivo, boolean cascade, Connection connect){
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
			retorno = InstituicaoIndicadorDAO.delete(cdInstituicao, cdIndicador, cdPeriodoLetivo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_indicador");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	


	public static ResultSetMap getAllByInstituicao(int cdInstituicao) {
		return getAllByInstituicao(cdInstituicao, null);
	}

	public static ResultSetMap getAllByInstituicao(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT A.*, B.nm_indicador, C.nm_periodo_letivo "
					+ "FROM acd_instituicao_indicador A "
					+ "JOIN acd_indicador B ON (A.cd_indicador = B.cd_indicador) "
					+ "JOIN acd_instituicao_periodo C ON (A.cd_periodo_letivo = C.cd_periodo_letivo) "
					+ "WHERE A.cd_instituicao = ? "
			);
			pstmt.setInt(1, cdInstituicao);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoIndicadorServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_indicador", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}