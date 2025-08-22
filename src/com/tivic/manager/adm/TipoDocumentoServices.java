package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TipoDocumentoServices	{

	public static final String DUPLICATA 	= "00";
	public static final String CHEQUE 		= "01";
	public static final String PROMISSORIA  = "02";
	public static final String RECIBO 		= "03";
	public static final String OUTROS 		= "99";

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;
	
	
	public static void init() {
		/*
		 * TIPOS DE DOCUMENTOS
		 */
		ArrayList<TipoDocumento> tipoDocs = new ArrayList<TipoDocumento>();
		tipoDocs.add(new TipoDocumento(0, "NP Nota Promissória", "NP", "11", 1, 0,"02"));
		tipoDocs.add(new TipoDocumento(0, "RC Recibo", "RC", "16", 1, 0,"03"));
		tipoDocs.add(new TipoDocumento(0, "ND Nota de Débito", "ND", "18", 1, 0,"99"));
		tipoDocs.add(new TipoDocumento(0, "CCR Cartão de Crédito", "CCR", "32", 1, 0,"99"));
		tipoDocs.add(new TipoDocumento(0, "DM Duplicata Mercantil", "DM", "1", 1, 0,"00"));
		tipoDocs.add(new TipoDocumento(0, "CH Cheque", "CH", "0", 1, 0,"01"));
		tipoDocs.add(new TipoDocumento(0, "FAT Fatura", "FAT", "17", 1, 0,"99"));
		tipoDocs.add(new TipoDocumento(0, "CCLI Crédito do Cliente", "CCLI", "30", 1, 0,"99"));
		tipoDocs.add(new TipoDocumento(0, "DARF Documento de Arrecadação Federal", "DARF", "DARF", 1, 0,"99"));
		tipoDocs.add(new TipoDocumento(0, "DAS Documento de Arrecadação do Simples", "DAS", "DAS", 1, 0,"99"));
		tipoDocs.add(new TipoDocumento(0, "GPS Guia de Previdência Social", "GPS", "GPS", 1, 0,"99"));
		tipoDocs.add(new TipoDocumento(0, "BOL Boleto Bancário", "BOL", "BOL", 1, 0,"99"));
		//
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_tipo_documento WHERE id_tipo_documento = ? OR sg_tipo_documento= ?");
			for(int i=0; i<tipoDocs.size(); i++)	{
				pstmt.setString(1, tipoDocs.get(i).getIdTipoDocumento());
				pstmt.setString(2, tipoDocs.get(i).getSgTipoDocumento());
				if(!pstmt.executeQuery().next())
					TipoDocumentoDAO.insert(tipoDocs.get(i), connect);
			}
			System.out.println("Inicialização de Tipos de Documentos concluida!");
			return;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
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
				return new Result(-1, "Erro ao salvar. TipoDocumento é nulo");

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
	
	public static ResultSetMap getAllAtivos() {
		return getAllAtivos(null);
	}

	public static ResultSetMap getAllAtivos(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
																"FROM adm_tipo_documento " +
																"WHERE st_tipo_documento = " + ST_ATIVO +
																"ORDER BY nm_tipo_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoServices.getAll: " + e);
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
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
																"FROM adm_tipo_documento " +
																"ORDER BY nm_tipo_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoServices.getAll: " + e);
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
		String nmTipoDocumento = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_TIPO_DOCUMENTO")) {
				nmTipoDocumento =	Util.limparTexto(criterios.get(i).getValue());
				nmTipoDocumento = nmTipoDocumento.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT * FROM adm_tipo_documento "+
							"WHERE 1=1 "+
						(!nmTipoDocumento.equals("") ?
							" AND TRANSLATE (nm_tipo_documento, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', "+
							"					'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmTipoDocumento)+"%' "
							: ""),
						criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}