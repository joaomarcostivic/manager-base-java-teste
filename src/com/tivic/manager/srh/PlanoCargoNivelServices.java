package com.tivic.manager.srh;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class PlanoCargoNivelServices {

	public static Result save(PlanoCargoNivel planoCargoNivel){
		return save(planoCargoNivel, null, null);
	}

	public static Result save(PlanoCargoNivel planoCargoNivel, AuthData authData){
		return save(planoCargoNivel, authData, null);
	}

	public static Result save(PlanoCargoNivel planoCargoNivel, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(planoCargoNivel==null)
				return new Result(-1, "Erro ao salvar. PlanoCargoNivel é nulo");

			int retorno;
			if(planoCargoNivel.getCdPlano()==0){
				retorno = PlanoCargoNivelDAO.insert(planoCargoNivel, connect);
				planoCargoNivel.setCdPlano(retorno);
			}
			else {
				retorno = PlanoCargoNivelDAO.update(planoCargoNivel, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOCARGONIVEL", planoCargoNivel);
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
	public static Result remove(int cdPlano, int cdNivel){
		return remove(cdPlano, cdNivel, false, null, null);
	}
	public static Result remove(int cdPlano, int cdNivel, boolean cascade){
		return remove(cdPlano, cdNivel, cascade, null, null);
	}
	public static Result remove(int cdPlano, int cdNivel, boolean cascade, AuthData authData){
		return remove(cdPlano, cdNivel, cascade, authData, null);
	}
	public static Result remove(int cdPlano, int cdNivel, boolean cascade, AuthData authData, Connection connect){
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
			retorno = PlanoCargoNivelDAO.delete(cdPlano, cdNivel, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_plano_cargo_nivel");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelServices.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_plano_cargo_nivel", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * Busca todas níveis atendidos por um determinado Plano de Cargo
	 * 
	 * @author Pádua
	 * @since 13/03/2017
	 */
	public static ResultSetMap getAllNivelByPlanoCargo(int cdPlanoCargo) {
		return getAllNivelByPlanoCargo(cdPlanoCargo, null);
	}

	public static ResultSetMap getAllNivelByPlanoCargo(int cdPlanoCargo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
		
			pstmt = connect.prepareStatement(
					"SELECT A.*, B.cd_nivel, B.nm_nivel, B.qt_vagas, B.vl_salario_base"
					+ " FROM srh_plano_cargo A"
					+ " JOIN srh_plano_cargo_nivel B ON (A.cd_plano = B.cd_plano and B.cd_plano = "+cdPlanoCargo+")"
			);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelServices.getAllNivelByPlanoCargo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelServices.getAllNivelByPlanoCargo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}