package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.interfaces.TabelasAuxiliares;

import sol.dao.ResultSetMap;

public class PrazosDashboardServices {
	
	private TabelasAuxiliaresMG tabelasAuxiliares;
	
	public PrazosDashboardServices() {
		tabelasAuxiliares = new TabelasAuxiliaresMG();
	}
	
	public ResultSetMap getMovimentosRegistroInfracao() throws Exception {
		return getMovimentosRegistroInfracao(null);
	}
	
	public ResultSetMap getMovimentosRegistroInfracao(Connection connect) throws Exception {
		Timestamp dtVencimento = new Timestamp(new GregorianCalendar(TimeZone.getTimeZone("GMT-3")).getTimeInMillis());
		return getMovimentosRegistroInfracao(connect, dtVencimento);
	}
	
	public ResultSetMap getMovimentosRegistroInfracao(Connection connect, Timestamp dtVencimento) throws Exception {
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			int prazo = tabelasAuxiliares.getPrazoMovimento(AitMovimentoServices.REGISTRO_INFRACAO);	
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT MOV.* FROM MOB_AIT_MOVIMENTO MOV JOIN MOB_AIT AIT ON (MOV.CD_AIT = AIT.CD_AIT) " +
					"WHERE MOV.TP_STATUS = ? AND MOV.LG_ENVIADO_DETRAN = ? AND AIT.DT_INFRACAO BETWEEN (DATE_TRUNC('DAY', ?::timestamp) - INTERVAL '" + prazo + " DAYS') " + 
					"AND (DATE_TRUNC('DAY', ?::timestamp) - INTERVAL '" + (prazo - 1) + " DAYS' - INTERVAL '1 SECOND')");
			
			pstmt.setInt(1, AitMovimentoServices.REGISTRO_INFRACAO);
			pstmt.setInt(2, AitMovimentoServices.NAO_ENVIADO);
			pstmt.setTimestamp(3, dtVencimento);
			pstmt.setTimestamp(4, dtVencimento);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
						
			return rsm;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public ResultSetMap getMovimentosNai() throws Exception {
		return getMovimentosNai(null);
	}
	
	public ResultSetMap getMovimentosNai(Connection connect) throws Exception {
		Timestamp dtVencimento = new Timestamp(new GregorianCalendar(TimeZone.getTimeZone("GMT-3")).getTimeInMillis());
		return getMovimentosNai(connect, dtVencimento);
	}
	
	public ResultSetMap getMovimentosNai(Connection connect, Timestamp dtVencimento) throws Exception {
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			int prazo = tabelasAuxiliares.getPrazoMovimento(AitMovimentoServices.NAI_ENVIADO);	
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT MOV.* FROM MOB_AIT_MOVIMENTO MOV JOIN MOB_AIT AIT ON (MOV.CD_AIT = AIT.CD_AIT) WHERE MOV.TP_STATUS = ? AND MOV.LG_ENVIADO_DETRAN = ? " +
					"AND AIT.DT_INFRACAO BETWEEN (DATE_TRUNC('DAY', ?::timestamp) - INTERVAL '" + prazo + " DAYS') AND " +
					"(DATE_TRUNC('DAY', ?::timestamp) - INTERVAL '" + (prazo - 1) + " DAYS' - INTERVAL '1 SECOND')");
			
			pstmt.setInt(1, AitMovimentoServices.NAI_ENVIADO);
			pstmt.setInt(2, AitMovimentoServices.NAO_ENVIADO);
			pstmt.setTimestamp(3, dtVencimento);
			pstmt.setTimestamp(4, dtVencimento);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
						
			return rsm;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public ResultSetMap getMovimentosNip() throws Exception {
		return getMovimentosNip(null);
	}
	
	public ResultSetMap getMovimentosNip(Connection connect) throws Exception {
		Timestamp dtVencimento = new Timestamp(new GregorianCalendar(TimeZone.getTimeZone("GMT-3")).getTimeInMillis());
		return getMovimentosNip(connect, dtVencimento);
	}
	
	public ResultSetMap getMovimentosNip(Connection connect, Timestamp dtVencimento) throws Exception {
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			int prazo = tabelasAuxiliares.getPrazoMovimento(AitMovimentoServices.NIP_ENVIADA);	
			System.out.println(dtVencimento.toString());
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT NIP.* FROM MOB_AIT_MOVIMENTO NIP JOIN MOB_AIT_MOVIMENTO NAI ON (NIP.CD_AIT = NAI.CD_AIT) WHERE NIP.TP_STATUS = ? " + 
					"AND NIP.LG_ENVIADO_DETRAN = ? AND NAI.TP_STATUS = ? AND NAI.LG_ENVIADO_DETRAN = ? AND NAI.DT_MOVIMENTO " + 
					"BETWEEN (DATE_TRUNC('DAY', ?::timestamp) - INTERVAL '" + prazo + " DAYS') AND (DATE_TRUNC('DAY', ?::timestamp) - INTERVAL '" + (prazo - 1) + " DAYS' - INTERVAL '1 SECOND')");
			
			pstmt.setInt(1, AitMovimentoServices.NIP_ENVIADA);
			pstmt.setInt(2, AitMovimentoServices.NAO_ENVIADO);
			pstmt.setInt(3, AitMovimentoServices.NAI_ENVIADO);
			pstmt.setInt(4, AitMovimentoServices.ENVIADO_AO_DETRAN);
			pstmt.setTimestamp(5, dtVencimento);
			pstmt.setTimestamp(6, dtVencimento);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
						
			return rsm;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
