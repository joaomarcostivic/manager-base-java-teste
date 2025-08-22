package com.tivic.manager.ptc;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class DocumentacaoObrigatoriaServices {

	public static Result save(DocumentacaoObrigatoria documentacaoObrigatoria){
		return save(documentacaoObrigatoria, null);
	}

	public static Result save(DocumentacaoObrigatoria documentacaoObrigatoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(documentacaoObrigatoria==null)
				return new Result(-1, "Erro ao salvar. DocumentacaoObrigatoria é nulo");

			int retorno;
			
			DocumentacaoObrigatoria d = DocumentacaoObrigatoriaDAO.get(documentacaoObrigatoria.getCdTipoDocumentacao(), documentacaoObrigatoria.getCdTipoDocumento(), connect);
			if(d==null){
				retorno = DocumentacaoObrigatoriaDAO.insert(documentacaoObrigatoria, connect);
			}
			else {
				retorno = DocumentacaoObrigatoriaDAO.update(documentacaoObrigatoria, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DOCUMENTACAOOBRIGATORIA", documentacaoObrigatoria);
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
	public static Result remove(int cdTipoDocumentacao, int cdTipoDocumento){
		return remove(cdTipoDocumentacao, cdTipoDocumento, false, null);
	}
	public static Result remove(int cdTipoDocumentacao, int cdTipoDocumento, boolean cascade){
		return remove(cdTipoDocumentacao, cdTipoDocumento, cascade, null);
	}
	public static Result remove(int cdTipoDocumentacao, int cdTipoDocumento, boolean cascade, Connection connect){
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
			retorno = DocumentacaoObrigatoriaDAO.delete(cdTipoDocumentacao, cdTipoDocumento, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTACAO_OBRIGATORIA");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para pegar a documentacao obrigatoria de acordo o documento 
	 * @param cdDocumento
	 * @return 
	 */
	public static ResultSetMap getAllByTipoDocumento(int cdDocumento) {
		return getAllByTipoDocumento(cdDocumento, null);
	}

	public static ResultSetMap getAllByTipoDocumento(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.* " + 
											" FROM ptc_documentacao_obrigatoria A" +
											" JOIN gpn_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento)" + 
											" JOIN grl_tipo_documentacao C ON (A.cd_tipo_documentacao = C.cd_tipo_documentacao)" +
											" WHERE A.cd_tipo_documento = " + cdDocumento);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM PTC_DOCUMENTACAO_OBRIGATORIA", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
