package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class BoatRelatorioAvariaServices {

	public static Result save(BoatRelatorioAvaria boatRelatorioAvaria){
		return save(boatRelatorioAvaria, null, null);
	}

	public static Result save(BoatRelatorioAvaria boatRelatorioAvaria, AuthData authData){
		return save(boatRelatorioAvaria, authData, null);
	}

	public static Result save(BoatRelatorioAvaria boatRelatorioAvaria, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(boatRelatorioAvaria==null)
				return new Result(-1, "Erro ao salvar. BoatRelatorioAvaria é nulo");

			int retorno;
			if(boatRelatorioAvaria.getCdVistoria()==0){
				retorno = BoatRelatorioAvariaDAO.insert(boatRelatorioAvaria, connect);
				boatRelatorioAvaria.setCdVistoria(retorno);
			}
			else {
				retorno = BoatRelatorioAvariaDAO.update(boatRelatorioAvaria, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOATRELATORIOAVARIA", boatRelatorioAvaria);
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
	public static Result remove(int cdVistoria, int cdVeiculo, int cdBoat){
		return remove(cdVistoria, cdVeiculo, cdBoat, false, null, null);
	}
	public static Result remove(int cdVistoria, int cdVeiculo, int cdBoat, boolean cascade){
		return remove(cdVistoria, cdVeiculo, cdBoat, cascade, null, null);
	}
	public static Result remove(int cdVistoria, int cdVeiculo, int cdBoat, boolean cascade, AuthData authData){
		return remove(cdVistoria, cdVeiculo, cdBoat, cascade, authData, null);
	}
	public static Result remove(int cdVistoria, int cdVeiculo, int cdBoat, boolean cascade, AuthData authData, Connection connect){
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
			retorno = BoatRelatorioAvariaDAO.delete(cdVistoria, cdVeiculo, cdBoat, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_relatorio_avaria");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_boat_relatorio_avaria", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}