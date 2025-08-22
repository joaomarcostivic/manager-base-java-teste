package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class SituacaoDocumentoServices {

	public static final String[] situacoesFixas = {"Em aberto", 
		"Fora da empresa",
		"Arquivado"};
	
	/* situações de documento pré-fixadas */
	public static final int ST_DOC_TODOS = -1;
	public static final int ST_DOC_ABERTO = 0;
	public static final int ST_DOC_ENVIO_EXTERNO = 1;
	public static final int ST_DOC_ARQUIVADO = 2;

	public static Result save(SituacaoDocumento situacaoDocumento){
		return save(situacaoDocumento, null);
	}
	
	public static Result save(SituacaoDocumento situacaoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(situacaoDocumento==null)
				return new Result(-1, "Erro ao salvar. Situação de documento é nulo");
			
			int retorno;
			if(situacaoDocumento.getCdSituacaoDocumento()==0){
				retorno = SituacaoDocumentoDAO.insert(situacaoDocumento, connect);
				situacaoDocumento.setCdSituacaoDocumento(retorno);
			}
			else {
				retorno = SituacaoDocumentoDAO.update(situacaoDocumento, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "SITUACAODOCUMENTO", situacaoDocumento);
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
	
	public static Result remove(int cdSituacaoDocumento){
		return remove(cdSituacaoDocumento, false, null);
	}
	
	public static Result remove(int cdSituacaoDocumento, boolean cascade){
		return remove(cdSituacaoDocumento, cascade, null);
	}
	
	public static Result remove(int cdSituacaoDocumento, boolean cascade, Connection connect){
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
				retorno = SituacaoDocumentoDAO.delete(cdSituacaoDocumento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este situação de documento está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Situação de documento excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir situação de documento!");
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_situacao_documento ORDER BY nm_situacao_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoDocumentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ptc_situacao_documento", "ORDER BY nm_situacao_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
