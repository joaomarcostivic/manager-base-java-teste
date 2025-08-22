package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class DocumentacaoServices {
		
	public static Result save(Documentacao documentacao){
		return save(documentacao, null);
	}
	
	public static Result save(Documentacao documentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(documentacao==null)
				return new Result(-1, "Erro ao salvar. Documentacao é nula");
			
			Result r;
			if(documentacao.getCdDocumentacao()==0) {
				r = insert(documentacao, connect);
				documentacao.setCdDocumentacao(r.getCode());
			}
			else
				r = update(documentacao, connect);
			
			if(r.getCode()<=0) {
				Conexao.rollback(connect);
				return r;
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(r.getCode(), "Salvo com sucesso.", "DOCUMENTACAO", documentacao);
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
	
	
	public static Result remove(int cdDocumento, int cdDocumentacao){
		return remove(cdDocumento, cdDocumentacao, false, null);
	}
	
	public static Result remove(int cdDocumento, int cdDocumentacao, boolean cascade){
		return remove(cdDocumento, cdDocumentacao, cascade, null);
	}
	
	public static Result remove(int cdDocumento, int cdDocumentacao, boolean cascade, Connection connect){
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
				retorno = DocumentacaoDAO.delete(cdDocumentacao, cdDocumento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta documentação está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Documentação excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir documentação!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result insert(Documentacao objeto){
		return insert(objeto, null);
	}
	
	public static sol.util.Result insert(Documentacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM ptc_documentacao " +
															   "WHERE cd_documento      = "+objeto.getCdDocumento()+
															   "  AND cd_tipo_documentacao = "+objeto.getCdTipoDocumentacao());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return new sol.util.Result(-1, "Esse tipo de documento já foi incluído neste documento!");
			int ret = DocumentacaoDAO.insert(objeto, connect);
			
			return new sol.util.Result(ret);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar incluir tipo de documento!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result update(Documentacao objeto){
		return update(objeto, null);
	}
	public static sol.util.Result update(Documentacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int ret = DocumentacaoDAO.update(objeto, connect);
			
			return new sol.util.Result(ret);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar incluir tipo de documento!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDocumentacaoOf(int cdDocumento)	{
		Connection connect = Conexao.conectar();
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_tipo_documento FROM ptc_documentacao A, grl_tipo_documento B " +
															 "WHERE A.cd_tipo_documento = B.cd_tipo_documento " +
															 "  AND A.cd_documento      = "+cdDocumento).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDocumentacaoOf(int cdDocumento, int cdTipoDocumento)	{
		Connection connect = Conexao.conectar();
		try {

			return new ResultSetMap(connect.prepareStatement("SELECT A.* FROM ptc_documentacao A " +
															 "WHERE A.cd_tipo_documento = "+cdTipoDocumento + 
															 "  AND A.cd_documento      = "+cdDocumento).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result updateStDocumentacao(int cdDocumentacao, int stDocumentacao, int cdDocumento){
	
		Connection connect = null;
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PTC_DOCUMENTACAO SET st_documentacao = ? WHERE cd_documentacao = ? AND cd_documento = ?");
			pstmt.setInt(1, stDocumentacao);
			pstmt.setInt(2, cdDocumentacao);
			pstmt.setInt(3, cdDocumento);
			pstmt.executeUpdate();
			return new sol.util.Result(1, "Atualizado com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar incluir tipo de documento!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	
	
	public static sol.util.Result insertAllTipoDocumentacao(int cdDocumento, int cdTipoDocumentoPtc){
		return insertAllTipoDocumentacao(cdDocumento, cdTipoDocumentoPtc, null);
	}
	@SuppressWarnings("unchecked")
	public static sol.util.Result insertAllTipoDocumentacao(int cdDocumento, int cdTipoDocumentoPtc, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			
			ResultSetMap rsm = getAllTipoDocumento(cdTipoDocumentoPtc);
			
			while(rsm.next()){
				HashMap<String,Object>[] keys = new HashMap[2];
				keys[0] = new HashMap<String,Object>();
				keys[0].put("FIELD_NAME", "cd_documentacao");
				keys[0].put("IS_KEY_NATIVE", "YES");
				keys[1] = new HashMap<String,Object>();
				keys[1].put("FIELD_NAME", "cd_documento");
				keys[1].put("IS_KEY_NATIVE", "NO");
				keys[1].put("FIELD_VALUE", new Integer(cdDocumento));
				int code = Conexao.getSequenceCode("ptc_documentacao", keys, connect);
				if (code <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new sol.util.Result(-1, "Erro ao tentar incluir tipo de documento!");
				}
				PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM ptc_documentacao " +
																   "WHERE cd_documento      = "+cdDocumento+
																   "  AND cd_tipo_documento = "+rsm.getInt("cd_tipo_documento_grl"));
				ResultSet rs = pstmt.executeQuery();
				if(rs.next())
					return new sol.util.Result(-1, "Já foi incluído todos os documentos relacionados!");
				int ret = DocumentacaoDAO.insert(
						new com.tivic.manager.ptc.Documentacao(
								code, cdDocumento, 0, new GregorianCalendar(), null, rsm.getInt("lg_original"), 1, null, rsm.getInt("st_documentacao"), 0, null),
								connect);
				
				if(ret < 0)
					return new sol.util.Result(-1, "Erro ao tentar incluir tipo de documento!");
			}
			
			
			
			return new sol.util.Result(1, "Incluidos com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar incluir tipo de documento!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static sol.util.Result insertTipoDocumentacao(int cdTipoDocumentoGrl, int cdTipoDocumentoPtc, int lgOriginal){
		return insertTipoDocumentacao(cdTipoDocumentoGrl, cdTipoDocumentoPtc, lgOriginal, null);
	}
	public static sol.util.Result insertTipoDocumentacao(int cdTipoDocumentoGrl, int cdTipoDocumentoPtc, int lgOriginal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PTC_TIPO_DOCUMENTACAO (cd_tipo_documento_grl,"+
											                    "cd_tipo_documento_ptc,"+
											                    "lg_original) VALUES (?, ?, ?)");
			pstmt.setInt(1, cdTipoDocumentoGrl);
			pstmt.setInt(2, cdTipoDocumentoPtc);
			pstmt.setInt(3, lgOriginal);
			pstmt.executeUpdate();
			return new sol.util.Result(1, "Incluido com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar incluir tipo de documento!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static sol.util.Result updateTipoDocumentacao(int cdTipoDocumentoGrl, int cdTipoDocumentoPtc, int lgOriginal){
		return updateTipoDocumentacao(cdTipoDocumentoGrl, cdTipoDocumentoPtc, lgOriginal, null);
	}
	public static sol.util.Result updateTipoDocumentacao(int cdTipoDocumentoGrl, int cdTipoDocumentoPtc, int lgOriginal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PTC_TIPO_DOCUMENTACAO SET lg_original = ? WHERE cd_tipo_documento_grl = ? AND cd_tipo_documento_ptc = ?");
			pstmt.setInt(1, lgOriginal);
			pstmt.setInt(2, cdTipoDocumentoGrl);
			pstmt.setInt(3, cdTipoDocumentoPtc);
			pstmt.executeUpdate();
			return new sol.util.Result(1, "Atualizado com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar incluir tipo de documento!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static sol.util.Result deleteTipoDocumentacao(int cdTipoDocumentoGrl, int cdTipoDocumentoPtc) {
		return deleteTipoDocumentacao(cdTipoDocumentoGrl, cdTipoDocumentoPtc, null);
	}

	public static sol.util.Result deleteTipoDocumentacao(int cdTipoDocumentoGrl, int cdTipoDocumentoPtc, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_tipo_documentacao WHERE cd_tipo_documento_grl=? AND cd_tipo_documento_ptc=?");
			pstmt.setInt(1, cdTipoDocumentoGrl);
			pstmt.setInt(2, cdTipoDocumentoPtc);
			pstmt.executeUpdate();
			return new sol.util.Result(1, "Excluido com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar incluir tipo de documento!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllTipoDocumento(int cdTipoDocumentoPtc)	{
		Connection connect = Conexao.conectar();
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.* "
					+ "FROM grl_tipo_documento A "
					+ "LEFT OUTER JOIN ptc_tipo_documentacao B ON (A.cd_tipo_documento = B.cd_tipo_documento_grl "
					+ "												AND B.cd_tipo_documento_ptc = "+cdTipoDocumentoPtc+")").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca toda a documentação de um documento
	 * 
	 * @param cdDocumento Código do documento
	 * @return ResultSetMap Lista de documentação
	 */
	public static ResultSetMap getAllByDocumento(int cdDocumento) {
		Connection connect  = Conexao.conectar();
		try {
			
			String sql = "SELECT A.*, B.nm_tipo_documentacao "
					+ " FROM ptc_documentacao A "
					+ " LEFT OUTER JOIN grl_tipo_documentacao B ON (A.cd_tipo_documentacao = B.cd_tipo_documentacao)"
					+ " WHERE A.cd_documento = " + cdDocumento;
				return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Salva lista de documentação em um documento
	 * 
	 * @param documentacoes ArrayList<Documentacoes> lista de Documentacao
	 * @param cdDocumento Código do Documento
	 * @return 
	 */
	public static Result saveDocumentacoes(ArrayList<Documentacao> documentacoes, int cdDocumento) {
		return saveDocumentacoes(documentacoes, cdDocumento, null);
	}
	
	public static Result saveDocumentacoes(ArrayList<Documentacao> documentacoes, int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			/*
			 * Excluir atual lista de documentações do documento
			 */
			Result r = removeByDocumento(cdDocumento, connect);
			
			if(r.getCode()<=0) {
				if (isConnectionNull)
					connect.rollback();
				
				return new Result(-3, r.getMessage());
			}	
			
			/*
			 * Insere nova lista de documentações
			 */
			for (Documentacao doc : documentacoes) {
				
				r = save(doc, connect);
				
				if(r.getCode()<=0) {
					Conexao.rollback(connect);
					System.out.println("DocumentacaoServices.saveDocumentacoes: "+r.getMessage());
					return new Result(-2, "Erro ao salvar documentação!");
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Documentações salvas com sucesso com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir documentações!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Exclui toda a documentação vinculada à um documento
	 * @param cdDocumento Código do documento
	 * @return
	 */
	public static Result removeByDocumento(int cdDocumento){
		return removeByDocumento(cdDocumento, null);
	}
	
	public static Result removeByDocumento(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			PreparedStatement pstmt = connect.prepareStatement(
					"DELETE FROM ptc_documentacao WHERE cd_documento = " +cdDocumento
			);
			
			retorno = pstmt.executeUpdate();
			
			if(retorno<0){
				Conexao.rollback(connect);
				return new Result(-2, "Documentação não pôde ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Documentação excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir documentação!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
}
