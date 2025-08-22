package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.TipoDocumentoPessoa;
import com.tivic.manager.grl.TipoDocumentoPessoaDAO;


public class TipoDocumentoPessoaServices {

	/* codificacao de erros retornados por rotinas da classe TipoDocumentoServices */
	/* tipo de documento já cadastrado */
	public static final int ERR_TIPO_DOC_CADASTRADO = -2; 

	public static int insert(TipoDocumentoPessoa documento) {
		return insert(documento, null);
	}

	public static int insert(TipoDocumentoPessoa documento, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			/* verifica se o tipo de documento já está cadastrado para a pessoa */
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM grl_tipo_documento_pessoa " +
					"WHERE cd_pessoa = ? " +
					"  AND cd_tipo_documento = ?");
			pstmt.setInt(1, documento.getCdPessoa());
			pstmt.setInt(2, documento.getCdTipoDocumento());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return ERR_TIPO_DOC_CADASTRADO;
			
			if (TipoDocumentoPessoaDAO.insert(documento, connection) <= 0)
				return -1;
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoPessoaServices.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int update(TipoDocumentoPessoa documento) {
		return update(documento, 0, 0, null);
	}

	public static int update(TipoDocumentoPessoa documento, int cdTipoDocumento, int cdPessoa) {
		return update(documento, cdTipoDocumento, cdPessoa, null);
	}

	public static int update(TipoDocumentoPessoa documento, Connection connection) {
		return update(documento, 0, 0, connection);
	}

	public static int update(TipoDocumentoPessoa documento, int cdTipoDocumentoOld, int cdPessoaOld, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			/* verifica se o tipo de documento já está cadastrado para a pessoa */
			if (cdTipoDocumentoOld != documento.getCdTipoDocumento()) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
						"FROM grl_tipo_documento_pessoa " +
						"WHERE cd_pessoa = ? " +
						"  AND cd_tipo_documento = ?");
				pstmt.setInt(1, documento.getCdPessoa());
				pstmt.setInt(2, documento.getCdTipoDocumento());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					return ERR_TIPO_DOC_CADASTRADO;				
			}
			
			if (TipoDocumentoPessoaDAO.update(documento, cdTipoDocumentoOld, cdPessoaOld, connection) <= 0)
				return -1;
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoPessoaServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result save(TipoDocumentoPessoa tipoDocumentoPessoa){
		return save(tipoDocumentoPessoa, null);
	}
	
	public static Result save(TipoDocumentoPessoa tipoDocumentoPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoDocumentoPessoa==null)
				return new Result(-1, "Erro ao salvar. Tipo de Documento é nulo");
			
			
			/* verifica se o tipo de documento já está cadastrado para a pessoa */
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM grl_tipo_documento_pessoa " +
					"WHERE cd_pessoa = ? " +
					"  AND cd_tipo_documento = ?");
			pstmt.setInt(1, tipoDocumentoPessoa.getCdPessoa());
			pstmt.setInt(2, tipoDocumentoPessoa.getCdTipoDocumento());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return new Result(-1, "Documento já cadastrado para esta pessoa!");;
						
			int retorno = TipoDocumentoPessoaDAO.insert(tipoDocumentoPessoa, connect);
			tipoDocumentoPessoa.setCdTipoDocumento(retorno);

			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPODOCUMENTOPESSOA", tipoDocumentoPessoa);
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
	
	public static Result remove(int cdTipoDocumentoPessoa, int cdPessoa){
		return remove(cdTipoDocumentoPessoa, cdPessoa, false, null);
	}
	
	public static Result remove(int cdTipoDocumentoPessoa, int cdPessoa, boolean cascade){
		return remove(cdTipoDocumentoPessoa, cdPessoa, cascade, null);
	}
	
	public static Result remove(int cdTipoDocumentoPessoa, int cdPessoa, boolean cascade, Connection connect){
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
				retorno = TipoDocumentoPessoaDAO.delete(cdTipoDocumentoPessoa, cdPessoa, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de documento está vinculada a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de Documento excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de documento!");
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_documento_pessoa ORDER BY cd_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM grl_tipo_documento_pessoa ORDER BY cd_documento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
}
