package com.tivic.manager.blb;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PublicacaoAutorServices {

	public static Result save(PublicacaoAutor publicacaoAutor){
		return save(publicacaoAutor, null);
	}

	public static Result save(PublicacaoAutor publicacaoAutor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(publicacaoAutor==null)
				return new Result(-1, "Erro ao salvar. PublicacaoAutor é nulo");

			int retorno;
			
			PublicacaoAutor autor = PublicacaoAutorDAO.get(publicacaoAutor.getCdAutor(), publicacaoAutor.getCdPublicacao(), connect);
			 
			if(autor==null){
				retorno = PublicacaoAutorDAO.insert(publicacaoAutor, connect);
			}
			else {
				retorno = PublicacaoAutorDAO.update(publicacaoAutor, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PUBLICACAOAUTOR", publicacaoAutor);
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
	public static Result remove(int cdAutor, int cdPublicacao){
		return remove(cdAutor, cdPublicacao, false, null);
	}
	public static Result remove(int cdAutor, int cdPublicacao, boolean cascade){
		return remove(cdAutor, cdPublicacao, cascade, null);
	}
	public static Result remove(int cdAutor, int cdPublicacao, boolean cascade, Connection connect){
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
			retorno = PublicacaoAutorDAO.delete(cdAutor, cdPublicacao, connect);
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
	
	public static Result removeByCdPublicacao(int cdPublicacao) {
		return removeByCdPublicacao(cdPublicacao, null);
	}

	public static Result removeByCdPublicacao(int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {		
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_publicacao_autor WHERE cd_publicacao = ?");
			pstmt.setInt(1, cdPublicacao);
			
			return new Result(pstmt.executeUpdate());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorServices.removeByCdPublicacao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorServices.removeByCdPublicacao: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_publicacao_autor");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorServices.getAll: " + e);
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
		return Search.find("SELECT * FROM blb_publicacao_autor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
