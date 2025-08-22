package com.tivic.manager.alm;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class DocumentoSaidaArquivoServices {

	
	public static final int ST_LIBERAR = 1;
	public static final int ST_FATURAR = 0;
	
	public static Result save(DocumentoSaidaArquivo documentoSaidaArquivo){
		return save(documentoSaidaArquivo, null);
	}

	public static Result save(DocumentoSaidaArquivo documentoSaidaArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(documentoSaidaArquivo==null)
				return new Result(-1, "Erro ao salvar. DocumentoSaidaArquivo é nulo");

			int retorno;
			if(documentoSaidaArquivo.getCdDocumentoSaida()==0){
				retorno = DocumentoSaidaArquivoDAO.insert(documentoSaidaArquivo, connect);
				documentoSaidaArquivo.setCdDocumentoSaida(retorno);
			}
			else {
				retorno = DocumentoSaidaArquivoDAO.update(documentoSaidaArquivo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DOCUMENTOSAIDAARQUIVO", documentoSaidaArquivo);
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
	public static Result remove(int cdContaReceber, int cdArquivo){
		return remove(cdContaReceber, cdArquivo, false, null);
	}
	public static Result remove(int cdContaReceber, int cdArquivo, boolean cascade){
		return remove(cdContaReceber, cdArquivo, cascade, null);
	}
	public static Result remove(int cdContaReceber, int cdArquivo, boolean cascade, Connection connect){
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
			retorno = DocumentoSaidaArquivoDAO.delete(cdContaReceber, cdArquivo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_documento_saida_arquivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_documento_saida_arquivo A"
				+ "			JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
