package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AbastecimentoEnergiaServices {

	
	public static Result save(AbastecimentoEnergia abastecimentoEnergia){
		return save(abastecimentoEnergia, null);
	}

	public static Result save(AbastecimentoEnergia abastecimentoEnergia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(abastecimentoEnergia==null)
				return new Result(-1, "Erro ao salvar. AbastecimentoEnergia é nulo");

			int retorno;
			if(abastecimentoEnergia.getCdAbastecimentoEnergia()==0){
				retorno = AbastecimentoEnergiaDAO.insert(abastecimentoEnergia, connect);
				abastecimentoEnergia.setCdAbastecimentoEnergia(retorno);
			}
			else {
				retorno = AbastecimentoEnergiaDAO.update(abastecimentoEnergia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ABASTECIMENTOENERGIA", abastecimentoEnergia);
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
	public static Result remove(int cdAbastecimentoEnergia){
		return remove(cdAbastecimentoEnergia, false, null);
	}
	public static Result remove(int cdAbastecimentoEnergia, boolean cascade){
		return remove(cdAbastecimentoEnergia, cascade, null);
	}
	public static Result remove(int cdAbastecimentoEnergia, boolean cascade, Connection connect){
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
			retorno = AbastecimentoEnergiaDAO.delete(cdAbastecimentoEnergia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_abastecimento_energia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_abastecimento_energia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static AbastecimentoEnergia getById(String idAbastecimentoEnergia) {
		return getById(idAbastecimentoEnergia, null);
	}

	public static AbastecimentoEnergia getById(String idAbastecimentoEnergia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_abastecimento_energia WHERE id_abastecimento_energia=?");
			pstmt.setString(1, idAbastecimentoEnergia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AbastecimentoEnergia(rs.getInt("cd_abastecimento_energia"),
						rs.getString("nm_abastecimento_energia"),
						rs.getString("id_abastecimento_energia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoEnergiaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
