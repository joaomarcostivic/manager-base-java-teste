package com.tivic.manager.fsc;

import java.sql.Connection;
import sol.dao.ResultSetMap;
import com.tivic.sol.connection.Conexao;

public class NotaFiscalDocVinculadoServices {

	public static ResultSetMap findByDocSaida(int cdDocumentoSaida) {
		return  findByDocSaida(cdDocumentoSaida, null);
	}
	
	public static ResultSetMap findByDocSaida(int cdDocumentoSaida, Connection connect) {
		boolean isConnectionNull = connect == null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		try{
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado A " +
															 "JOIN fsc_nota_fiscal B ON (A.cd_nota_fiscal = B.cd_nota_fiscal " +
															 "                       AND B.st_nota_fiscal <> "+NotaFiscalServices.DENEGADA+"" +
															 "                       AND B.st_nota_fiscal <> "+NotaFiscalServices.CANCELADA+") " +
															 "WHERE A.cd_documento_saida = "+cdDocumentoSaida).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findByDocEntrada(int cdDocumentoEntrada) {
		return  findByDocSaida(cdDocumentoEntrada, null);
	}
	
	public static ResultSetMap findByDocEntrada(int cdDocumentoEntrada, Connection connect) {
		boolean isConnectionNull = connect == null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		try{
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado A " +
															 "JOIN fsc_nota_fiscal B ON (A.cd_nota_fiscal = B.cd_nota_fiscal " +
															 "                       AND B.st_nota_fiscal <> "+NotaFiscalServices.DENEGADA+"" +
															 "                       AND B.st_nota_fiscal <> "+NotaFiscalServices.CANCELADA+") " +
															 "WHERE A.cd_documento_entrada = "+cdDocumentoEntrada).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findByNotaFiscal(int cdNotaFiscal) {
		return  findByNotaFiscal(cdNotaFiscal, null);
	}
	
	public static ResultSetMap findByNotaFiscal(int cdNotaFiscal, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
		String tpDoc = (nota.getTpMovimento() == NotaFiscalServices.MOV_ENTRADA) ? "entrada" : "saida";
		if(isConnectionNull)
			connect = Conexao.conectar();
		try{
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado A " +
															 "JOIN alm_documento_"+tpDoc+" B ON (A.cd_documento_"+tpDoc+" = B.cd_documento_"+tpDoc+") " +
															 "WHERE A.cd_nota_fiscal = "+cdNotaFiscal).executeQuery());
			if(rsm.size() == 0){
				tpDoc = (nota.getTpMovimento() != NotaFiscalServices.MOV_ENTRADA) ? "entrada" : "saida";
				return new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado A " +
						 "JOIN alm_documento_"+tpDoc+" B ON (A.cd_documento_"+tpDoc+" = B.cd_documento_"+tpDoc+") " +
						 "WHERE A.cd_nota_fiscal = "+cdNotaFiscal).executeQuery());
			}
			
			return rsm;
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
