package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;

import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class NivelCobrancaServices {
	
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;
	
	public static ResultSetMap getAllByCobranca(int cdCobranca){
		return getAllByCobranca(cdCobranca, null);
	}
	
	public static ResultSetMap getAllByCobranca(int cdCobranca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT * FROM adm_nivel_cobranca WHERE cd_cobranca = " + cdCobranca;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap get(int cdNivelCobranca, int cdCobranca){
		return get(cdNivelCobranca, cdCobranca, null);
	}
	
	public static ResultSetMap get(int cdNivelCobranca, int cdCobranca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT * FROM adm_nivel_cobranca WHERE cd_nivel_cobranca = "+cdNivelCobranca+" AND cd_cobranca = " + cdCobranca;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getNivelCobranca(int cdCobranca, int cdCliente){
		return getNivelCobranca(cdCobranca, cdCliente, null);
	}
	
//	public static ResultSetMap getNivelCobranca(int cdCobranca, int cdCliente, Connection connect){
//		boolean isConnectionNull = connect==null;
//		try {
//			connect = isConnectionNull ? Conexao.conectar() : connect;
//			
//			String sqlDividas = "SELECT A.dt_documento_saida, (vl_conta - (vl_recebido + vl_abatimento - B.vl_acrescimo)) AS vl_divida, A.vl_total_documento, dt_vencimento, cd_conta_receber FROM alm_documento_saida A " +  
//								"LEFT OUTER JOIN adm_conta_receber B ON (A.cd_documento_saida = B.cd_documento_saida) " +
//								"WHERE cd_conta_origem IS NULL  " +
//								" AND (vl_conta is null OR (vl_recebido + vl_abatimento - B.vl_acrescimo) < vl_conta) " +
//								" AND cd_cliente = ?  " +
//								" AND st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
//								" ORDER BY dt_documento_Saida LIMIT 1";
//			PreparedStatement pstmtDividas = connect.prepareStatement(sqlDividas);
//			pstmtDividas.setInt(1, cdCliente);
//			
//			String sqlContasReceber = "SELECT dt_vencimento FROM adm_conta_receber " +
//					"					WHERE cd_conta_origem = ? " +
//					"					ORDER BY dt_vencimento LIMIT 1";
//			PreparedStatement pstmtContasReceber = connect.prepareStatement(sqlContasReceber);
//			
//			
//			ResultSetMap rsmDividas = new ResultSetMap(pstmtDividas.executeQuery());
//			ResultSetMap rsm = new ResultSetMap();
//			
//			if(rsmDividas.next()){
//				if(rsmDividas.getInt("cd_conta_receber") > 0){
//					pstmtContasReceber.setInt(1, rsmDividas.getInt("cd_conta_receber"));
//					ResultSetMap rsmContasReceber = new ResultSetMap(pstmtContasReceber.executeQuery());
//					boolean entrou = false;
//					if(rsmContasReceber.next()){
//						entrou = true;
//						incluirRegistroNivelCobranca(rsmContasReceber.getGregorianCalendar("dt_vencimento"), cdCobranca, rsmDividas.getFloat("vl_divida"), rsmDividas.getFloat("vl_total_documento"), rsm, connect);
//					}
//					if(!entrou)
//						incluirRegistroNivelCobranca(rsmDividas.getGregorianCalendar("dt_vencimento"), cdCobranca, rsmDividas.getFloat("vl_divida"), rsmDividas.getFloat("vl_total_documento"), rsm, connect);
//				}
//				else{
//					incluirRegistroNivelCobranca(rsmDividas.getGregorianCalendar("dt_documento_saida"), cdCobranca, rsmDividas.getFloat("vl_divida"), rsmDividas.getFloat("vl_total_documento"), rsm, connect);
//				}
//				
//			}
//			
//			return rsm;
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			System.err.println("Erro! CobrancaServices.get: " + e);
//			return null;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
//	}
//	
//	public static void incluirRegistroNivelCobranca(GregorianCalendar dtVencimento, int cdCobranca, float vlDivida, float vlTotalDocumento, ResultSetMap rsm, Connection connect){
//		
//		try{
//			String sqlNivelCobranca = "SELECT * FROM adm_nivel_cobranca " +
//					"					WHERE qt_dias_apos_vencimento < ? " +
//					"					  AND cd_cobranca = ? " +
//					"					ORDER BY qt_dias_apos_vencimento, cd_nivel_cobranca DESC LIMIT 1";
//			PreparedStatement pstmtNivelCobranca = connect.prepareStatement(sqlNivelCobranca);
//			
//			int qtDiferenca = Util.getQuantidadeDiasUteis2(dtVencimento, Util.getDataAtual(), connect) - 1;
//			pstmtNivelCobranca.setInt(1, qtDiferenca);
//			pstmtNivelCobranca.setInt(2, cdCobranca);
//			ResultSetMap rsmNivelCobranca = new ResultSetMap(pstmtNivelCobranca.executeQuery());
//			if(rsmNivelCobranca.next()){
//				HashMap<String, Object> register = rsmNivelCobranca.getRegister();
//				register.put("QT_DIAS_ATRASO", qtDiferenca);
//				register.put("VL_DIVIDA", (vlDivida > 0 ? vlDivida : vlTotalDocumento));
//				rsm.addRegister(register);
//			}
//		
//		}
//		
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		
//	}
	
	public static ResultSetMap getNivelCobranca(int cdCobranca, int cdCliente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			String sqlDividas = "SELECT A.dt_documento_saida, dt_vencimento, cd_conta_receber FROM alm_documento_saida A " +  
								"LEFT OUTER JOIN adm_conta_receber B ON (A.cd_documento_saida = B.cd_documento_saida) " +
								"WHERE cd_conta_origem IS NULL  " +
								" AND (vl_conta is null OR (vl_recebido + vl_abatimento - B.vl_acrescimo) < vl_conta) " +
								" AND cd_cliente = ?  " +
								" AND st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
								" ORDER BY dt_documento_Saida LIMIT 1";
			PreparedStatement pstmtDividas = connect.prepareStatement(sqlDividas);
			pstmtDividas.setInt(1, cdCliente);
			
			String sqlContasReceber = "SELECT dt_vencimento FROM adm_conta_receber " +
					"					WHERE cd_conta_origem = ? " +
					"					ORDER BY dt_vencimento LIMIT 1";
			PreparedStatement pstmtContasReceber = connect.prepareStatement(sqlContasReceber);
			
			
			ResultSetMap rsmDividas = new ResultSetMap(pstmtDividas.executeQuery());
			ResultSetMap rsm = new ResultSetMap();
			
			if(rsmDividas.next()){
				if(rsmDividas.getInt("cd_conta_receber") > 0){
					pstmtContasReceber.setInt(1, rsmDividas.getInt("cd_conta_receber"));
					ResultSetMap rsmContasReceber = new ResultSetMap(pstmtContasReceber.executeQuery());
					boolean entrou = false;
					if(rsmContasReceber.next()){
						entrou = true;
						incluirRegistroNivelCobranca(rsmContasReceber.getGregorianCalendar("dt_vencimento"), cdCobranca, rsmDividas.getFloat("vl_divida"), rsmDividas.getFloat("vl_total_documento"), rsm, connect);
					}
					if(!entrou)
						incluirRegistroNivelCobranca(rsmDividas.getGregorianCalendar("dt_vencimento"), cdCobranca, rsmDividas.getFloat("vl_divida"), rsmDividas.getFloat("vl_total_documento"), rsm, connect);
				}
				else{
					incluirRegistroNivelCobranca(rsmDividas.getGregorianCalendar("dt_documento_saida"), cdCobranca, rsmDividas.getFloat("vl_divida"), rsmDividas.getFloat("vl_total_documento"), rsm, connect);
				}
				
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void incluirRegistroNivelCobranca(GregorianCalendar dtVencimento, int cdCobranca, float vlDivida, float vlTotalDocumento, ResultSetMap rsm, Connection connect){
		
		try{
			String sqlNivelCobranca = "SELECT * FROM adm_nivel_cobranca " +
					"					WHERE qt_dias_apos_vencimento < ? " +
					"					  AND cd_cobranca = ? " +
					"					ORDER BY qt_dias_apos_vencimento, cd_nivel_cobranca DESC LIMIT 1";
			PreparedStatement pstmtNivelCobranca = connect.prepareStatement(sqlNivelCobranca);
			
			int qtDiferenca = Util.getQuantidadeDiasUteis(dtVencimento, Util.getDataAtual(), connect);
			pstmtNivelCobranca.setInt(1, qtDiferenca);
			pstmtNivelCobranca.setInt(2, cdCobranca);
			ResultSetMap rsmNivelCobranca = new ResultSetMap(pstmtNivelCobranca.executeQuery());
			if(rsmNivelCobranca.next()){
				HashMap<String, Object> register = rsmNivelCobranca.getRegister();
				register.put("QT_DIAS_ATRASO", qtDiferenca);
				register.put("VL_DIVIDA", (vlDivida > 0 ? vlDivida : vlTotalDocumento));
				rsm.addRegister(register);
			}
		
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
