package com.tivic.manager.grl;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.AbastecimentoAgua;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.log.Sistema;
import com.tivic.manager.log.SistemaDAO;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.srh.LotacaoServices;
import com.tivic.manager.util.Util;

public class TipoDocumentacaoServices {

	public static final String TP_NIS 		 	 = "01";
	public static final String TP_RG 		 	 = "02";
	public static final String TP_PASSAPORTE 	 = "03";
	public static final String TP_REG_NASCIMENTO = "04";
	public static final String TP_REG_CASAMENTO  = "05";
	public static final String TP_CPF            = "06";
	public static final String TP_SUS            = "07";
	
	public static Result save(TipoDocumentacao tipoDocumentacao){
		return save(tipoDocumentacao, null);
	}

	public static Result save(TipoDocumentacao tipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoDocumentacao==null)
				return new Result(-1, "Erro ao salvar. TipoDocumentacao é nulo");

			int retorno;
			if(tipoDocumentacao.getCdTipoDocumentacao()==0){
				retorno = TipoDocumentacaoDAO.insert(tipoDocumentacao, connect);
				tipoDocumentacao.setCdTipoDocumentacao(retorno);
			}
			else {
				retorno = TipoDocumentacaoDAO.update(tipoDocumentacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPODOCUMENTACAO", tipoDocumentacao);
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
	public static Result remove(int cdTipoDocumentacao){
		return remove(cdTipoDocumentacao, false, null);
	}
	public static Result remove(int cdTipoDocumentacao, boolean cascade){
		return remove(cdTipoDocumentacao, cascade, null);
	}
	public static Result remove(int cdTipoDocumentacao, boolean cascade, Connection connect){
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
			retorno = TipoDocumentacaoDAO.delete(cdTipoDocumentacao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_documentacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * 
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
			pstmt = connect.prepareStatement("SELECT A.* FROM grl_tipo_documentacao A" +
											" JOIN ptc_documentacao_obrigatoria B ON (A.cd_tipo_documentacao = B.cd_tipo_documentacao)" +
											" WHERE B.cd_tipo_documento = " + cdDocumento);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_documentacao", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static TipoDocumentacao getById(String idTipoDocumentacao) {
		return getById(idTipoDocumentacao, null);
	}

	public static TipoDocumentacao getById(String idTipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_documentacao WHERE id_tipo_documentacao=?");
			pstmt.setString(1, idTipoDocumentacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
//				return new TipoDocumentacao(rs.getInt("cd_tipo_documentacao"),
//						rs.getString("nm_tipo_documentacao"),
//						rs.getString("id_tipo_documentacao"));
				
//				return new TipoDocumentacao(rs.getInt("CD_TIPO_DOCUMENTACAO"),
//						rs.getString("NM_TIPO_DOCUMENTACAO"),
//						rs.getString("ID_TIPO_DOCUMENTACAO"));
				
				return new TipoDocumentacao(rs.getInt("CD_TIPO_DOCUMENTACAO"),
						rs.getString("NM_TIPO_DOCUMENTACAO"),
						rs.getString("SG_TIPO_DOCUMENTACAO"),
						rs.getInt("CD_FORMULARIO"),
						rs.getString("ID_TIPO_DOCUMENTACAO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * Método que busca o formulário dinâmico correspondente
	 * a um tipo de documento
	 * 
	 * @param cdTipoDocumento tipo de documento
	 * @return ResultSetMap com o formulário dinâmico
	 * @author Maurício
	 * @since 18/05/2015
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
	 * Busca os tipos de documentação que possuem formulários dinâmicos
	 * 
	 * @return ResultSetMap
	 * @author Maurício
	 * @since 15/05/2015
	 */
	public static ResultSetMap getTipoDocumentacaoFormulario() {
		return getTipoDocumentacaoFormulario(null);
	}

	public static ResultSetMap getTipoDocumentacaoFormulario(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					" SELECT A.*, B.nm_formulario, B.id_formulario " +
					" FROM grl_tipo_documentacao A " +
					" LEFT JOIN grl_formulario B ON (A.cd_formulario = B.cd_formulario) " +
					" WHERE A.cd_formulario IS NOT NULL" +
					" ORDER BY nm_tipo_documentacao ");

			return (new ResultSetMap(pstmt.executeQuery()));
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	/**
	 * Busca os atributos de um formulario a partir do tipo de documentação
	 * 
	 * @param cdTipoDocumentacao Código do Tipo da Documentação
	 * @return ResultSetMap atributos do formulário
	 */
	public static ResultSetMap getAllAtributos(int cdTipoDocumentacao) {
		return getAllAtributos(cdTipoDocumentacao, null);
	}

	public static ResultSetMap getAllAtributos(int cdTipoDocumentacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT B.* FROM grl_tipo_documentacao A, grl_formulario_atributo B " +
															   "WHERE A.cd_tipo_documentacao = " +cdTipoDocumentacao+
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
	
	
	public static Result corrigirNumeroCertidaoModeloNovo() {
		return corrigirNumeroCertidaoModeloNovo(null);
	}

	public static Result corrigirNumeroCertidaoModeloNovo(Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_tipo_documentacao WHERE nr_documento like '%XX%'");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				String nrDv = Util.getMod11(rsm.getString("nr_documento").substring(0, 30));
				connect.prepareStatement("UPDATE grl_pessoa_tipo_documentacao set nr_documento = '"+rsm.getString("nr_documento").substring(0, 30) + nrDv +"' WHERE cd_pessoa = " + rsm.getInt("cd_pessoa") + " AND cd_tipo_documentacao = " + rsm.getInt("cd_tipo_documentacao")).executeUpdate();
			}
			rsm.beforeFirst();
			
			if(isConnectionNull)
				connect.commit();
			
			
			return new Result(1);
			
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return new Result(-3, "Erro ao autenticar...");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}
	
	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM grl_tipo_documentacao";
			
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT * FROM grl_tipo_documentacao";
			pstmt = connect.prepareStatement(sql);
			
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
	

}
