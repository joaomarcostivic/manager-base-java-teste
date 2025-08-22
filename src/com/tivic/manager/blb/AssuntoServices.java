package com.tivic.manager.blb;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AssuntoServices {
	
	public static int TP_ASSUNTO_CDD = 0;
	public static int TP_ASSUNTO_CDU = 1;

	public static Result save(Assunto assunto){
		return save(assunto, null);
	}

	public static Result save(Assunto assunto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(assunto==null)
				return new Result(-1, "Erro ao salvar. Assunto é nulo");

			int retorno;
			if(assunto.getCdAssunto()==0){
				retorno = AssuntoDAO.insert(assunto, connect);
				assunto.setCdAssunto(retorno);
			}
			else {
				retorno = AssuntoDAO.update(assunto, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ASSUNTO", assunto);
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
	public static Result remove(int cdAssunto){
		return remove(cdAssunto, false, null);
	}
	public static Result remove(int cdAssunto, boolean cascade){
		return remove(cdAssunto, cascade, null);
	}
	public static Result remove(int cdAssunto, boolean cascade, Connection connect){
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
			retorno = AssuntoDAO.delete(cdAssunto, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_assunto");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByTpAssunto(int tpAssunto) {
		return getAllByTpAssunto(tpAssunto, null);
	}

	public static ResultSetMap getAllByTpAssunto(int tpAssunto, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_assunto WHERE tp_tabela_assunto = ? ORDER BY id_assunto");
			pstmt.setInt(1, tpAssunto);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoServices.getAllByTpAssunto: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoServices.getAllByTpAssunto: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByPublicacao(int cdPublicacao) {
		return getAllByPublicacao(cdPublicacao, null);
	}

	public static ResultSetMap getAllByPublicacao(int cdPublicacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.* FROM blb_assunto A " +
											 " JOIN blb_publicacao_assunto B ON (A.cd_assunto = B.cd_assunto)" +
											 " JOIN blb_publicacao C ON (B.cd_publicacao = C.cd_publicacao AND C.cd_publicacao=?)");
			pstmt.setInt(1, cdPublicacao);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoServices.getAllByPublicacao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoServices.getAllByPublicacao: " + e);
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
		return Search.find("SELECT * FROM blb_assunto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
