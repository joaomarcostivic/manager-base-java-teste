package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class BoatOcorrenciaServices {

	public static Result save(BoatOcorrencia boatOcorrencia){
		return save(boatOcorrencia, null, null);
	}

	public static Result save(BoatOcorrencia boatOcorrencia, AuthData authData){
		return save(boatOcorrencia, authData, null);
	}

	public static Result save(BoatOcorrencia boatOcorrencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(boatOcorrencia==null)
				return new Result(-1, "Erro ao salvar. BoatOcorrencia � nulo");

			int retorno;
			if(boatOcorrencia.getCdBoatOcorrencia()==0){
				retorno = BoatOcorrenciaDAO.insert(boatOcorrencia, connect);
				boatOcorrencia.setCdBoatOcorrencia(retorno);
			}
			else {
				retorno = BoatOcorrenciaDAO.update(boatOcorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOATOCORRENCIA", boatOcorrencia);
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
	public static Result remove(BoatOcorrencia boatOcorrencia) {
		return remove(boatOcorrencia.getCdBoatOcorrencia());
	}
	public static Result remove(int cdBoatOcorrencia){
		return remove(cdBoatOcorrencia, false, null, null);
	}
	public static Result remove(int cdBoatOcorrencia, boolean cascade){
		return remove(cdBoatOcorrencia, cascade, null, null);
	}
	public static Result remove(int cdBoatOcorrencia, boolean cascade, AuthData authData){
		return remove(cdBoatOcorrencia, cascade, authData, null);
	}
	public static Result remove(int cdBoatOcorrencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = BoatOcorrenciaDAO.delete(cdBoatOcorrencia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_boat_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static void gerarOcorrencia(Boat boat, Connection connect) {
		try {
			BoatOcorrencia boatOcorrencia = new BoatOcorrencia();
			boatOcorrencia.setCdBoat(boat.getCdBoat());
			boatOcorrencia.setDtOcorrencia(new GregorianCalendar());
			boatOcorrencia.setDsOcorrencia("Registro de declaração N° " + boat.getNrBoat());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaServices.gerarOcorrencia: " + e);
		}
	}

}
