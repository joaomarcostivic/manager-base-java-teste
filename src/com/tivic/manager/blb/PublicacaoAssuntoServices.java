package com.tivic.manager.blb;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;
import sol.dao.Conexao;

public class PublicacaoAssuntoServices {

	public static Result save(PublicacaoAssunto publicacaoAssunto){
		return save(publicacaoAssunto, null);
	}

	public static Result save(PublicacaoAssunto publicacaoAssunto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(publicacaoAssunto==null)
				return new Result(-1, "Erro ao salvar. PublicacaoAssunto é nulo");

			int retorno;
			
			PublicacaoAssunto assunto = PublicacaoAssuntoDAO.get(publicacaoAssunto.getCdAssunto(), publicacaoAssunto.getCdPublicacao(), connect);
			
			if(assunto==null){
				retorno = PublicacaoAssuntoDAO.insert(publicacaoAssunto, connect);
				publicacaoAssunto.setCdAssunto(retorno);
			}
			else {
				retorno = PublicacaoAssuntoDAO.update(publicacaoAssunto, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PUBLICACAOASSUNTO", publicacaoAssunto);
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
	public static Result remove(int cdAssunto, int cdPublicacao){
		return remove(cdAssunto, cdPublicacao, false, null);
	}
	public static Result remove(int cdAssunto, int cdPublicacao, boolean cascade){
		return remove(cdAssunto, cdPublicacao, cascade, null);
	}
	public static Result remove(int cdAssunto, int cdPublicacao, boolean cascade, Connection connect){
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
			retorno = PublicacaoAssuntoDAO.delete(cdAssunto, cdPublicacao, connect);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_publicacao_assunto WHERE cd_publicacao = ?");
			pstmt.setInt(1, cdPublicacao);
			
			return new Result(pstmt.executeUpdate());

		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoServices.removeByCdPublicacao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoServices.removeByCdPublicacao: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_publicacao_assunto");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM blb_publicacao_assunto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
