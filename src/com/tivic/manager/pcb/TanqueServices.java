package com.tivic.manager.pcb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.adm.ContaFechamentoDAO;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@DestinationConfig(enabled = false)
public class TanqueServices {

	public static ResultSetMap getTanques(int cdEmpresa){
		return TanqueDAO.getAllTanques(cdEmpresa);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
			return TanqueDAO.findTanque(criterios);
	}
	
	public static ResultSetMap getTanquesOf(int cdProdutoServico, int cdDocumentoEntrada) {
		return getTanquesOf(cdProdutoServico, cdDocumentoEntrada, null);
	}

	public static ResultSetMap getTanquesOf(int cdProdutoServico, int cdDocumentoEntrada, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ResultSetMap rsm = null;
			
			GregorianCalendar diaAnterior = Util.getDataAtual();
			diaAnterior.add(Calendar.DAY_OF_MONTH, -1);
			pstmt = connect.prepareStatement("SELECT T.cd_produto_servico, T.cd_tanque, E.qt_entrada, TT.qt_capacidade, LA.cd_local_armazenamento, LA.nm_local_armazenamento " +
											 "FROM pcb_tanque T " + 
				       						 "JOIN alm_local_armazenamento           LA ON (T.cd_tanque = LA.cd_local_armazenamento)  " + 
				       						 "JOIN pcb_tipo_tanque                   TT ON (T.cd_tipo_tanque = TT.cd_tipo_tanque)  " + 
				       						 "LEFT OUTER JOIN alm_entrada_local_item E  ON (E.cd_produto_servico = " +  cdProdutoServico +  
				       						 "                                          AND E.cd_documento_entrada = " +  cdDocumentoEntrada + 
				       						 "										    AND E.cd_local_armazenamento = T.cd_tanque)  " +
				       						 "WHERE T.cd_produto_servico = " +  cdProdutoServico +  
											 "ORDER BY nm_local_armazenamento ");
			
			rs = pstmt.executeQuery();
			rsm = new ResultSetMap(rs);
			while(rsm.next()){
				rsm.setValueToField("QT_PREENCHIDA", 0);
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("CAST(dt_fechamento AS DATE)", Util.formatDate(diaAnterior, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.TIMESTAMP));
				ResultSetMap rsmContaFechamento = ContaFechamentoDAO.find(criterios, connect);
				int contagem = 365;
				while(!rsmContaFechamento.next() && contagem > 0){
					criterios = new ArrayList<ItemComparator>();
					diaAnterior.add(Calendar.DAY_OF_MONTH, -1);
					criterios.add(new ItemComparator("CAST(dt_fechamento AS DATE)", Util.formatDate(diaAnterior, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.TIMESTAMP));
					rsmContaFechamento = ContaFechamentoDAO.find(criterios, connect);
					contagem--;
				}
				do{
					if(contagem == 0)
						break;
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_fechamento", "" + rsmContaFechamento.getInt("cd_fechamento"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmMedicaFisica = MedicaoFisicaDAO.find(criterios, connect);
					boolean encontradoTanque = false;
					while(rsmMedicaFisica.next()){
						if(rsmMedicaFisica.getInt("cd_tanque") == rsm.getInt("cd_tanque")){
							encontradoTanque = true;
							ResultSetMap rsmDocEntrada = new ResultSetMap(connect.prepareStatement("SELECT SUM(qt_entrada + qt_entrada_consignada) AS qt_entrada FROM alm_entrada_local_item ELI "
									+ "																  JOIN alm_documento_entrada DE ON (DE.cd_documento_entrada = ELI.cd_documento_entrada) "
									+ "																  WHERE DE.dt_documento_entrada = '" + Util.convCalendarStringSql(Util.getDataAtual()) + "' "
									+ "																 	AND DE.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO
									+ "																	AND ELI.cd_local_armazenamento = " + rsm.getInt("cd_tanque")).executeQuery());
							float qtEntrada = 0;
							if(rsmDocEntrada.next()){
								qtEntrada = rsmDocEntrada.getFloat("qt_entrada");
							}
							
//							ResultSetMap rsmDocSaida = new ResultSetMap(connect.prepareStatement("SELECT SUM(qt_saida + qt_saida_consignada) AS qt_saida FROM alm_saida_local_item ELI "
//									+ "																  JOIN alm_documento_saida DE ON (DE.cd_documento_saida = ELI.cd_documento_saida) "
//									+ "																  WHERE CAST(DE.dt_documento_saida AS DATE) = '" + Util.convCalendarStringSql(Util.getDataAtual()) + "' "
//									+ "																 	AND DE.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO
//									+ "																	AND ELI.cd_local_armazenamento = " + rsm.getInt("cd_tanque")).executeQuery());
//							float qtSaida = 0;
//							if(rsmDocSaida.next()){
//								qtSaida = rsmDocSaida.getFloat("qt_saida");
//							}
							rsm.setValueToField("QT_PREENCHIDA", (rsmMedicaFisica.getFloat("qt_estoque_escritural") + qtEntrada));
							break;
						}
					}
					
					if(encontradoTanque)
						break;
					else{
						criterios = new ArrayList<ItemComparator>();
						diaAnterior.add(Calendar.DAY_OF_MONTH, -1);
						criterios.add(new ItemComparator("CAST(dt_fechamento AS DATE)", Util.formatDate(diaAnterior, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.TIMESTAMP));
						rsmContaFechamento = ContaFechamentoDAO.find(criterios, connect);
						contagem--;
					}
						
				}while(rsmContaFechamento.next());
			}
			rsm.beforeFirst();
			return rsm;
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
	
	public static boolean verificaProdutoTanque(int cdCombustivel, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		return verificaProdutoTanque(cdCombustivel, dtInicial, dtFinal, null);
	}
	
	public static boolean verificaProdutoTanque(int cdCombustivel, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect){
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			dtInicial.set(Calendar.HOUR, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			
			dtFinal.set(Calendar.HOUR, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM pcb_bico_encerrante A, adm_conta_fechamento B " +
					                                           "WHERE A.cd_fechamento  = B.cd_fechamento" +
					                                           "  AND A.cd_conta       = B.cd_conta " +
					                                           "  AND A.cd_combustivel = " + cdCombustivel+
					                                           "  AND B.dt_fechamento BETWEEN ? AND ? ");
			pstmt.setTimestamp(1, new java.sql.Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new java.sql.Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
		} 
		catch (SQLException e) {
			e.printStackTrace(System.out);
			return false;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ArrayList<Integer> getAllAsArray(){
		ArrayList<Integer> codigos = new ArrayList<Integer>();
		ResultSetMap rsm = TanqueDAO.getAll();
		while(rsm.next()){
			codigos.add(rsm.getInt("cd_tanque"));
		}
		return codigos;
	}
	
	public static boolean verificaLocalTanque(String cdLocalArmazenamento){
		return verificaLocalTanque(cdLocalArmazenamento, null);
	}
	
	public static boolean verificaLocalTanque(String cdLocalArmazenamento, Connection connect){
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM pcb_tanque A " +
					                                           "WHERE A.cd_tanque IN ("+cdLocalArmazenamento+")");
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
		} 
		catch (SQLException e) {
			e.printStackTrace(System.out);
			return false;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
}