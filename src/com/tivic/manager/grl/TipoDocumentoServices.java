package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoDocumentoServices {
	public static Result save(TipoDocumento tipoDocumento){
		return save(tipoDocumento, null);
	}
	
	public static Result save(TipoDocumento tipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoDocumento==null)
				return new Result(-1, "Erro ao salvar. Tipo de documento é nulo");
			
			int retorno;
			if(tipoDocumento.getCdTipoDocumento()==0){
				retorno = TipoDocumentoDAO.insert(tipoDocumento, connect);
				tipoDocumento.setCdTipoDocumento(retorno);
			}
			else {
				retorno = TipoDocumentoDAO.update(tipoDocumento, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPODOCUMENTO", tipoDocumento);
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
	
	public static Result remove(int cdTipoDocumento){
		return remove(cdTipoDocumento, false, null);
	}
	
	public static Result remove(int cdTipoDocumento, boolean cascade){
		return remove(cdTipoDocumento, cascade, null);
	}
	
	public static Result remove(int cdTipoDocumento, boolean cascade, Connection connect){
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
				retorno = TipoDocumentoDAO.delete(cdTipoDocumento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de documento está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de documento excluído com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_documento ORDER BY nm_tipo_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_documento", "ORDER BY nm_tipo_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * Método que busca o formulário dinâmico correspondente
	 * a um tipo de documento
	 * 
	 * @param cdTipoDocumento tipo de documento
	 * @return ResultSetMap com o formulário dinâmico
	 * @author Maurício
	 * @since 22/07/2014
	 */
	public static Result getFormulario(int cdTipoDocumento, String idModulo) {
		return getFormulario(cdTipoDocumento, idModulo, null);
	}
	
	public static Result getFormulario(int cdTipoDocumento, String idModulo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			Result result = null;
			
			// Busca o formulário do tipo de documento em ptc.TipoDocumento
			if(idModulo.equals("ptc")) {
				result = com.tivic.manager.ptc.TipoDocumentoServices.getFormulario(cdTipoDocumento, connect);
			}
			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca os tipos de documento que possuem formulários dinâmicos
	 * 
	 * @return ResultSetMap
	 * @author Maurício
	 * @since 25/07/2014
	 */
	public static ResultSetMap getTipoDocumentoFormulario() {
		return getTipoDocumentoFormulario(null);
	}

	public static ResultSetMap getTipoDocumentoFormulario(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					" SELECT A.*, B.nm_formulario, B.id_formulario " +
					" FROM grl_tipo_documento A " +
					" LEFT JOIN grl_formulario B ON (A.cd_formulario = B.cd_formulario) " +
					" WHERE A.cd_formulario IS NOT NULL" +
					" ORDER BY nm_tipo_documento ");

			return (new ResultSetMap(pstmt.executeQuery()));
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	/**
	 * Busca os atributos de um formulario a partir do tipo de documento
	 * 
	 * @param cdTipoDocumento Código do Tipo do Documento
	 * @return ResultSetMap atributos do formulário
	 */
	public static ResultSetMap getAllAtributos(int cdTipoDocumento) {
		return getAllAtributos(cdTipoDocumento, null);
	}

	public static ResultSetMap getAllAtributos(int cdTipoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT B.* FROM grl_tipo_documento A, grl_formulario_atributo B " +
															   "WHERE A.cd_tipo_documento = " +cdTipoDocumento+
															   "  AND A.cd_formulario     = B.cd_formulario " +
															   "ORDER BY nr_ordem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
