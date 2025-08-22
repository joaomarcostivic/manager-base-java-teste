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
import com.tivic.manager.seg.AuthData;

public class BoatTestemunhaServices {

	public static Result save(int cdBoat, ArrayList<BoatTestemunha> testemunhas) {
		return save(cdBoat, testemunhas, null);
	}

	public static Result save(int cdBoat, ArrayList<BoatTestemunha> testemunhas, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int retorno = 0;
			for (BoatTestemunha testemunha: testemunhas) {
				testemunha.setCdBoat(cdBoat);
				testemunha.setCdPessoa(0);
				
				retorno = save(testemunha, connect).getCode();
				
				if(retorno<=0)
					break;
			}
			
			if(retorno>0)
				return new Result(1, "Testemunha(s) incluídos com sucesso.");
			else
				return new Result(-1, "Erro ao incluir testemunha(s).");
				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao incluir testemunha(s).");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(BoatTestemunha boatTestemunha){
		return save(boatTestemunha, null);
	}

	public static Result save(BoatTestemunha boatTestemunha, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(boatTestemunha==null)
				return new Result(-1, "Erro ao salvar. BoatTestemunha é nulo");

			if(boatTestemunha.getPessoa()==null)
				return new Result(-2, "Erro ao salvar testemunha. Pessoa é nula.");
			
			int retorno;
			
			if(BoatTestemunhaDAO.get(boatTestemunha.getCdBoat(), boatTestemunha.getCdPessoa(), connect) == null){
				
				Pessoa pessoa = boatTestemunha.getPessoa();
				pessoa.setCdPessoa(0);
				
				retorno = PessoaDAO.insert(pessoa, connect);
				
				if(retorno > 0) {
					boatTestemunha.setCdPessoa(retorno);
					retorno = BoatTestemunhaDAO.insert(boatTestemunha, connect);
					boatTestemunha.setCdBoat(retorno);
				}
				
			}
			else {
				PessoaDAO.update(boatTestemunha.getPessoa(), connect);
				retorno = BoatTestemunhaDAO.update(boatTestemunha, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOATTESTEMUNHA", boatTestemunha);
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
			retorno = BoatTestemunhaDAO.delete(cdBoat, cdPessoa, connect);
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
			
			int retorno = connect.prepareStatement("DELETE FROM mob_boat_testemunha WHERE cd_boat="+cdBoat).executeUpdate();
			
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_testemunha");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllTestemunhaByBoat(int cdBoat) {
		return getAllTestemunhaByBoat(cdBoat, null);
	}

	public static ResultSetMap getAllTestemunhaByBoat(int cdBoat, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		
		try {
			pstmt = connect.prepareStatement(
					" SELECT A.*,"
				  + " B.nm_pessoa, B.gn_pessoa, B.nr_telefone1 "
				  + " FROM mob_boat_testemunha A"
				  + " JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)"
				  + " WHERE A.cd_boat = ?");
			pstmt.setInt(1, cdBoat);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaServices.getAllTestemunhaByBoat: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaServices.getAllTestemunhaByBoat: " + e);
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
		return Search.find("SELECT A.*, B.nm_pessoa, C.nr_rg, C.dt_nascimento FROM mob_boat_testemunha A, grl_pessoa B, grl_pessoa_fisica C WHERE A.cd_pessoa = B.cd_pessoa AND A.cd_pessoa = C.cd_pessoa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}