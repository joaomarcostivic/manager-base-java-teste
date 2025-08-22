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

public class BoatInconsistenciaServices {

	public static Result save(BoatInconsistencia boatInconsistencia){
		return save(boatInconsistencia, null, null);
	}

	public static Result save(BoatInconsistencia boatInconsistencia, AuthData authData){
		return save(boatInconsistencia, authData, null);
	}

	public static Result save(BoatInconsistencia boatInconsistencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(boatInconsistencia==null)
				return new Result(-1, "Erro ao salvar. BoatInconsistencia � nulo");

			int retorno;
			retorno = BoatInconsistenciaDAO.insert(boatInconsistencia, connect);
			boatInconsistencia.setCdBoat(retorno);

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOATINCONSISTENCIA", boatInconsistencia);
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
	public static Result remove(BoatInconsistencia boatInconsistencia) {
		return remove(boatInconsistencia.getCdBoat(), boatInconsistencia.getCdInconsistencia());
	}
	public static Result remove(int cdBoat, int cdInconsistencia){
		return remove(cdBoat, cdInconsistencia, false, null, null);
	}
	public static Result remove(int cdBoat, int cdInconsistencia, boolean cascade){
		return remove(cdBoat, cdInconsistencia, cascade, null, null);
	}
	public static Result remove(int cdBoat, int cdInconsistencia, boolean cascade, AuthData authData){
		return remove(cdBoat, cdInconsistencia, cascade, authData, null);
	}
	public static Result remove(int cdBoat, int cdInconsistencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = BoatInconsistenciaDAO.delete(cdBoat, cdInconsistencia, connect);
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
	

	public static Result remove(int cdBoat, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cdBoat <= 0) {
				return new Result(-1, "É necessário informar o BOAT a ser removido.");
			}
			
			PreparedStatement pstmt = connect.prepareStatement("delete from mob_boat_inconsistencia where cd_boat = ?");
			pstmt.setInt(1, cdBoat);
			
			if(pstmt.executeUpdate()<0){
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_inconsistencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatInconsistenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_boat_inconsistencia A, mob_inconsistencia B WHERE A.cd_inconsistencia = B.cd_inconsistencia ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
