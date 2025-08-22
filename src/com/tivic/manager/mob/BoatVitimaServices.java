package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.seg.AuthData;

public class BoatVitimaServices {

	public static Result save(int cdBoat, ArrayList<BoatVitima> vitimas) {
		return save(cdBoat, vitimas, null);
	}

	public static Result save(int cdBoat, ArrayList<BoatVitima> vitimas, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int retorno = 0;
			for (BoatVitima vitima: vitimas) {
				vitima.setCdBoat(cdBoat);
				vitima.setCdPessoa(0);
				
				retorno = save(vitima, connect).getCode();
				
				if(retorno<=0)
					break;
			}
			
			if(retorno>0)
				return new Result(1, "Vitima(s) incluídos com sucesso.");
			else
				return new Result(-1, "Erro ao incluir vitima(s).");
				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao incluir vitima(s).");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result save(BoatVitima boatVitima){
		return save(boatVitima, null);
	}


	public static Result save(BoatVitima boatVitima, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(boatVitima==null)
				return new Result(-1, "Erro ao salvar. BoatVitima é nulo");

			if(boatVitima.getPessoa()==null)
				return new Result(-2, "Erro ao salvar vítima. Pessoa é nula.");
			
			int retorno;
			if(BoatVitimaDAO.get(boatVitima.getCdBoat(), boatVitima.getCdPessoa(), connect)==null){
				
				Pessoa pessoa = boatVitima.getPessoa();
				pessoa.setCdPessoa(0);
				
				retorno = PessoaDAO.insert(pessoa, connect);
				
				if(retorno > 0) {
					boatVitima.setCdPessoa(retorno);
					retorno = BoatVitimaDAO.insert(boatVitima, connect);
					boatVitima.setCdBoat(retorno);
				}
			}
			else {
				PessoaDAO.update(boatVitima.getPessoa(), connect);				
				retorno = BoatVitimaDAO.update(boatVitima, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOATVITIMA", boatVitima);
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
	public static Result remove(int cdBoat, int cdPessoa){
		return remove(cdBoat, cdPessoa, false, null, null);
	}
	public static Result remove(int cdBoat, int cdPessoa, boolean cascade){
		return remove(cdBoat, cdPessoa, cascade, null, null);
	}
	public static Result remove(int cdBoat, int cdPessoa, boolean cascade, AuthData authData){
		return remove(cdBoat, cdPessoa, cascade, authData, null);
	}
	public static Result remove(int cdBoat, int cdPessoa, boolean cascade, AuthData authData, Connection connect){
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
			retorno = BoatVitimaDAO.delete(cdBoat, cdPessoa, connect);
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
	
	public static Result removeAll(int cdBoat, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = connect.prepareStatement("DELETE FROM mob_boat_vitima WHERE cd_boat="+cdBoat).executeUpdate();
			
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_vitima");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllVitimasByBoat(int cdBoat) {
		return getAllVitimasByBoat(cdBoat, null);
	}

	public static ResultSetMap getAllVitimasByBoat(int cdBoat, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			pstmt = connect.prepareStatement(
					  " SELECT  A.*, "
					+ " B.nm_pessoa, B.gn_pessoa, B.nr_telefone1 "
					+ " FROM mob_boat_vitima A"
					+ " JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)"
					+ " WHERE A.cd_boat = ?");
			pstmt.setInt(1, cdBoat);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaServices.getAllVitimasByBoat: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaServices.getAllVitimasByBoat: " + e);
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
		return Search.find("SELECT * FROM mob_boat_vitima", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}