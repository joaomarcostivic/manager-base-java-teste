package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.manager.adm.ContaReceber;
import com.tivic.manager.adm.ContaReceberDAO;
import com.tivic.manager.adm.ContaReceberServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.egov.DAMUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class DocumentoContaServices {

	public static Result save(DocumentoConta documentoConta){
		return save(documentoConta, null);
	}

	public static Result save(DocumentoConta documentoConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(documentoConta==null)
				return new Result(-1, "Erro ao salvar. DocumentoConta é nulo");

			int retorno;
			
			if(documentoConta.getCdDocumentoConta()==0){
				retorno = DocumentoContaDAO.insert(documentoConta, connect);
				documentoConta.setCdDocumentoConta(retorno);
			}
			else {
				retorno = DocumentoContaDAO.update(documentoConta, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DOCUMENTOCONTA", documentoConta);
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

	public static Result remove(int cdDocumento, int cdDocumentoConta){
		return remove(cdDocumento, cdDocumentoConta, false, null);
	}
	public static Result remove(int cdDocumento, int cdDocumentoConta, boolean cascade){
		return remove(cdDocumento, cdDocumentoConta, cascade, null);
	}
	public static Result remove(int cdDocumento, int cdDocumentoConta, boolean cascade, Connection connect){
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
			retorno = DocumentoContaDAO.delete(cdDocumento, cdDocumentoConta, connect);
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
	
	/**
	 * Inativa o documentoConta, podendo tambem cancelar a taxa no banco da EeL e cancelar conta a receber
	 * @param cdDocumento
	 * @param cdDocumentoConta
	 * @param lgCancelarTaxaEL
	 * @param lgCancelarConta
	 */
	public static Result setInativo(int cdDocumento, int cdDocumentoConta){
		return setInativo(cdDocumento, cdDocumentoConta, false, false, null);
	}
	
	public static Result setInativo(int cdDocumento, int cdDocumentoConta, boolean lgCancelarTaxaEL, boolean lgCancelarConta){
		return setInativo(cdDocumento, cdDocumentoConta, lgCancelarTaxaEL, lgCancelarConta, null);
	}
	
	public static Result setInativo(int cdDocumento, int cdDocumentoConta, boolean lgCancelarTaxaEL, boolean lgCancelarConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			int confirmacao = 0;
			
			/**
			 * Setar DocumentoConta como inativo
			 */
			DocumentoConta dc = DocumentoContaDAO.get(cdDocumento, cdDocumentoConta, connect);
			dc.setStDocumentoConta(DocumentoConta.ST_INATIVO);
			retorno = DocumentoContaDAO.update(dc, connect);
			
			/**
			 * Cancelar conta a receber
			 */
			if(lgCancelarConta && retorno>0) {
				ContaReceber cr = ContaReceberDAO.get(dc.getCdContaReceber());
				cr.setStConta(ContaReceberServices.ST_CANCELADA);
				retorno = ContaReceberDAO.update(cr, connect);
			}
			
			/**
			 * Excluir taxa no banco E&L - PROCON
			 */
			if(lgCancelarTaxaEL && !(dc.getNrDocumento()==null || dc.getNrDocumento()=="") && retorno>0){
				
				String codigoG = null; // id da empresa no banco da E&L
				String codigoTxc = null; // codigo da taxa (DAM)
				String ano;
				PreparedStatement pstmt;
				
				// Criar novo documentoConta ativo
				DocumentoConta novoDc = new DocumentoConta(dc.getCdDocumento(), 0, dc.getCdContaReceber(), 0, null, null, 0, null, DocumentoConta.ST_ATIVO);
				Result result = save(novoDc, connect);
				confirmacao = result.getCode(); 
				
				// Buscar codigo da pessoa e codigo da taxa no banco PROCON
				pstmt = connect.prepareStatement("select A.cod_movimento, C.cd_pessoa_externa, B.dt_emissao" +
													" from ptc_documento_conta A" +
													" left outer join adm_conta_receber B on (A.cd_conta_receber = B.cd_conta_receber)" +
													" left outer join grl_pessoa_externa C on (B.cd_pessoa = C.cd_pessoa)" +
													" where A.cd_documento = ?" + 
													" and A.cd_documento_conta = ?");
				pstmt.setInt(1, cdDocumento);
				pstmt.setInt(2, cdDocumentoConta);
				
				ResultSet rs;
				rs = pstmt.executeQuery();
				
				if(rs.next()){
					codigoG = rs.getString("cd_pessoa_externa");
					codigoTxc = rs.getString("cod_movimento");
					ano = rs.getDate("dt_emissao").getYear() + "";
					
					if(codigoTxc != null)
						confirmacao = DAMUtils.deleteTaxa(codigoG, ano, codigoTxc);
				}
			}
			
			if(retorno<=0 || (lgCancelarTaxaEL && confirmacao<=0)){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser cancelado!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro cancelado com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao cancelar registro!");
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_conta");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
		
	public static ResultSetMap getAllByDocumento(int cdDocumento) {
		return getAllByDocumento(cdDocumento, null);
	}

	public static ResultSetMap getAllByDocumento(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, R.*, B.*, C.*, E.nr_documento as nr_notificacao," +
											" D.nr_cnpj, D.nm_razao_social, D.nr_inscricao_estadual, nr_inscricao_municipal," + // pessoa juridica
											" P.ds_endereco, P.nm_logradouro, P.nm_bairro, P.nr_endereco, P.nm_complemento, U.nm_cidade, V.sg_estado, V.nm_estado" + // endereco
											" FROM ptc_documento_conta A" +
											" LEFT OUTER JOIN adm_conta_receber R ON (A.cd_conta_receber = R.cd_conta_receber)" +
											" LEFT OUTER JOIN ptc_documento E ON (A.cd_documento = E.cd_documento)" +
											" LEFT OUTER JOIN grl_pessoa B ON (R.cd_pessoa = B.cd_pessoa)" +
											" LEFT OUTER JOIN grl_pessoa_fisica C ON (R.cd_pessoa = C.cd_pessoa)" +
											" LEFT OUTER JOIN grl_pessoa_juridica D ON (R.cd_pessoa = D.cd_pessoa)" +
											" LEFT OUTER JOIN grl_pessoa_endereco P ON (R.cd_pessoa = P.cd_pessoa AND P.lg_principal = 1)" +
											" LEFT OUTER JOIN grl_cidade U ON (P.cd_cidade = U.cd_cidade)" +
											" LEFT OUTER JOIN grl_estado V ON (U.cd_estado = V.cd_estado) " +
										//	" LEFT adm_conta_pagar C ON (A.cd_conta_pagar = C.cd_conta_pagar)" +
											" WHERE A.cd_documento = " + cdDocumento +
											" AND A.st_documento_conta = " + DocumentoConta.ST_ATIVO);
				
				//System.out.println(pstmt);
			
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				
				while(rsm.next()){
					rsm.setValueToField("VL_CONTA_EXTENSO", Util.formatExtenso(rsm.getFloat("VL_CONTA"), true));
				}
				
				//System.out.println(rsm);
				
				return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaServices.getAllByDocumento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaServices.getAllByDocumento: " + e);
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
		return Search.find("SELECT * FROM ptc_documento_conta", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
