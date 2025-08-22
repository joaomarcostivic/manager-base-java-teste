package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class BoatRelacaoServices {

	public static Result save(BoatRelacao boatRelacao){
		return save(boatRelacao, null, null);
	}

	public static Result save(BoatRelacao boatRelacao, AuthData authData){
		return save(boatRelacao, authData, null);
	}

	public static Result save(BoatRelacao boatRelacao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(boatRelacao==null)
				return new Result(-1, "Erro ao salvar. BoatRelacao � nulo");

			int retorno;
			retorno = BoatRelacaoDAO.insert(boatRelacao, connect);

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOATRELACAO", boatRelacao);
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
	public static Result remove(BoatRelacao boatRelacao) {
		return remove(boatRelacao.getCdBoat(), boatRelacao.getCdBoatRelacao());
	}
	public static Result remove(int cdBoat, int cdBoatRelacao){
		return remove(cdBoat, cdBoatRelacao, false, null, null);
	}
	public static Result remove(int cdBoat, int cdBoatRelacao, boolean cascade){
		return remove(cdBoat, cdBoatRelacao, cascade, null, null);
	}
	public static Result remove(int cdBoat, int cdBoatRelacao, boolean cascade, AuthData authData){
		return remove(cdBoat, cdBoatRelacao, cascade, authData, null);
	}
	public static Result remove(int cdBoat, int cdBoatRelacao, boolean cascade, AuthData authData, Connection connect){
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
			retorno = BoatRelacaoDAO.delete(cdBoat, cdBoatRelacao, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro est� vinculado a outros e n�o pode ser exclu�do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro exclu�do com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_relacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getBoatsRelacionados(int cdBoat, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_relacao WHERE cd_boat = ? or cd_boat_relacao=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdBoat);

			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoServices.getBoatsRelacionados: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoServices.getBoatsRelacionados: " + e);
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
		return Search.findAndLog("SELECT * FROM mob_boat_relacao", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
