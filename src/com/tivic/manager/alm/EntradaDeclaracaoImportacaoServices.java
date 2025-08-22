package com.tivic.manager.alm;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class EntradaDeclaracaoImportacaoServices {

	/*Tipo de Via de Transporte*/
	public static int TP_VIA_MARITIMA 			 = 1; 
	public static int TP_VIA_FLUVIAL 			 = 2; 
	public static int TP_VIA_LACUSTRE   		 = 3; 
	public static int TP_VIA_AEREA 				 = 4; 
	public static int TP_VIA_POSTAL 			 = 5; 
	public static int TP_VIA_FERROVIARIA 		 = 6; 
	public static int TP_VIA_RODOVIARIA 		 = 7; 
	public static int TP_VIA_CONDUTO 			 = 8; 
	public static int TP_VIA_MEIOS_PROPRIOS 	 = 9; 
	public static int TP_VIA_ENTRADA_SAIDA_FICTA = 10;
	
	public static final String[] tiposViaTransporte = {"Selecione...", "Marítima", "Fluvial", "Lacustre", "Aérea", "Postal", "Ferroviária", "Rodoviária", "Conduto", "Meios Próprios", "Entrada/Saída Ficta"};
	
	/*Tipo de Intermedio*/
	public static int TP_INTERMEDIO_PROPRIA  	 = 1; 
	public static int TP_INTERMEDIO_CONTA_ORDEM	 = 2; 
	public static int TP_INTERMEDIO_ENCOMENDA    = 3;
	
	public static final String[] tiposIntermedio = {"Selecione...", "Por conta próprio", "Por conta e ordem", "Por encomenda"};
	
	public static Result save(EntradaDeclaracaoImportacao entradaDeclaracaoImportacao){
		return save(entradaDeclaracaoImportacao, null);
	}

	public static Result save(EntradaDeclaracaoImportacao entradaDeclaracaoImportacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(entradaDeclaracaoImportacao==null)
				return new Result(-1, "Erro ao salvar. EntradaDeclaracaoImportacao é nulo");

			int retorno;
			if(entradaDeclaracaoImportacao.getCdEntradaDeclaracaoImportacao()==0){
				retorno = EntradaDeclaracaoImportacaoDAO.insert(entradaDeclaracaoImportacao, connect);
				entradaDeclaracaoImportacao.setCdEntradaDeclaracaoImportacao(retorno);
			}
			else {
				retorno = EntradaDeclaracaoImportacaoDAO.update(entradaDeclaracaoImportacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ENTRADADECLARACAOIMPORTACAO", entradaDeclaracaoImportacao);
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
	public static Result remove(int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada){
		return remove(cdEntradaDeclaracaoImportacao, cdDocumentoEntrada, false, null);
	}
	public static Result remove(int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada, boolean cascade){
		return remove(cdEntradaDeclaracaoImportacao, cdDocumentoEntrada, cascade, null);
	}
	public static Result remove(int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada, boolean cascade, Connection connect){
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
			retorno = EntradaDeclaracaoImportacaoDAO.delete(cdEntradaDeclaracaoImportacao, cdDocumentoEntrada, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_entrada_declaracao_importacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_entrada_declaracao_importacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAllAdicoes(int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_entrada_declaracao_importacao", "" + cdEntradaDeclaracaoImportacao, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = EntradaAdicaoServices.find(criterios);
		while(rsm.next()){
			rsm.setValueToField("VL_IPI", rsm.getFloat("VL_BASE_CALCULO_IPI") * rsm.getFloat("PR_ALIQUOTA_IPI") / 100);
			rsm.setValueToField("VL_II", rsm.getFloat("VL_BASE_CALCULO_II") * rsm.getFloat("PR_ALIQUOTA_II") / 100);
		}
		rsm.beforeFirst();
		return rsm;
	}
}
