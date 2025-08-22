package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class AitPagamentoReportServices {

	public static ResultSetMap reportPeriodo(LocalDate dtInicial, LocalDate dtFinal) {
		return reportPeriodo(dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap reportPeriodo(LocalDate dtInicial, LocalDate dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm;
			String sql = "SELECT A.dt_pagamento, ROUND(CAST(SUM(A.vl_pago) AS decimal (16,2)), 2) AS vl_pago, COUNT(A.cd_ait) AS quantidade FROM mob_ait_pagamento A INNER JOIN mob_ait B ON B.cd_ait = A.cd_ait "
					+ "WHERE A.dt_pagamento BETWEEN '"+dtInicial+ "' AND '"+dtFinal+ " 23:59:59' GROUP BY (dt_pagamento)";

			pstmt = connect.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}	catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportPeriodo: " + sqlExpt);
			return null;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportPeriodo: " +  e);
			return null;
		}   finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap reportData(LocalDate dtPagamento) {
		return reportData(dtPagamento, null);
	}
	
	public static ResultSetMap reportData(LocalDate dtPagamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm;
			LocalDate next = dtPagamento.plusDays(1);
			String sql = "SELECT B.id_ait, A.vl_pago, C.tp_status, C.lg_enviado_detran, dt_pagamento FROM mob_ait_pagamento A "
					+ "INNER JOIN mob_ait B ON B.cd_ait = A.cd_ait "
					+ "INNER JOIN mob_ait_movimento C ON C.cd_ait = A.cd_ait "
					+ "WHERE A.dt_pagamento >= '"+dtPagamento+ "' AND A.dt_pagamento <'"+next+"' "
					+ "AND (C.tp_status ="+AitMovimentoServices.MULTA_PAGA+" "
					+ "OR C.tp_status ="+AitMovimentoServices.MULTA_PAGA_OUTRA_UF+")";

			pstmt = connect.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}	catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportData: " + sqlExpt);
			return null;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportData: " +  e);
			return null;
		}   finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap reportTramitacao(LocalDate dtInicial, LocalDate dtFinal) {
		return reportTramitacao(dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap reportTramitacao(LocalDate dtInicial, LocalDate dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm;
			String sql = "SELECT COUNT(A.cd_ait) quantidade, A.sg_uf_veiculo, ROUND(CAST(SUM(A.vl_multa) AS decimal (16,2)), 2) AS vl_total " + 
					"FROM mob_ait A " + 
					"INNER JOIN mob_ait_movimento B ON B.cd_movimento = A.cd_movimento_atual AND B.cd_ait = A.cd_ait " + 
					"WHERE A.sg_uf_veiculo IS NOT NULL AND " +
					"B.tp_status !="+AitMovimentoServices.MULTA_PAGA+" AND B.tp_status != "+AitMovimentoServices.MULTA_PAGA_OUTRA_UF + " "+
					"AND A.dt_infracao BETWEEN '"+dtInicial+ "' AND '"+dtFinal+ " 23:59:59' "+
					"GROUP BY A.sg_uf_veiculo";
			pstmt = connect.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}	catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportTramitacao: " + sqlExpt);
			return null;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportTramitacao: " +  e);
			return null;
		}   finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap reportEstado(LocalDate dtInicial, LocalDate dtFinal, String sgUf) {
		return reportEstado(dtInicial, dtFinal, sgUf, null);
	}
	
	public static ResultSetMap reportEstado(LocalDate dtInicial, LocalDate dtFinal, String sgUf, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm;
			String sql = "SELECT A.id_ait, A.dt_infracao, A.dt_vencimento, A.vl_multa, B.tp_status, B.lg_enviado_detran "
					+ "FROM mob_ait A "
					+ "INNER JOIN mob_ait_movimento B ON B.cd_ait = A.cd_ait AND B.cd_movimento = A.cd_movimento_atual "
					+ "WHERE A.sg_uf_veiculo =? "
					+ "AND B.tp_status !="+AitMovimentoServices.MULTA_PAGA+" AND B.tp_status != "+AitMovimentoServices.MULTA_PAGA_OUTRA_UF + " "
					+ "AND A.dt_infracao BETWEEN '"+dtInicial+ "' AND '"+dtFinal+ " 23:59:59' "
					+ "ORDER BY A.dt_infracao DESC";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, sgUf);
			rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}	catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportEstado: " + sqlExpt);
			return null;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportEstado: " +  e);
			return null;
		}   finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap reportVencidas(LocalDate dtInicial, LocalDate dtFinal) {
		return reportVencidas(dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap reportVencidas(LocalDate dtInicial, LocalDate dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm;
			String sql = "SELECT COUNT(A.cd_ait) quantidade, A.sg_uf_veiculo, ROUND(CAST(SUM(A.vl_multa) AS decimal (16,2)), 2) AS vl_total " + 
					"FROM mob_ait A " + 
					"INNER JOIN mob_ait_movimento B ON B.cd_movimento = A.cd_movimento_atual AND B.cd_ait = A.cd_ait " + 
					"WHERE A.sg_uf_veiculo IS NOT NULL " +
					"AND A.dt_vencimento IS NOT NULL "+
					"AND B.tp_status !="+AitMovimentoServices.MULTA_PAGA+" AND B.tp_status != "+AitMovimentoServices.MULTA_PAGA_OUTRA_UF + " "+
					"AND A.dt_vencimento BETWEEN '"+dtInicial+ "' AND '"+dtFinal+ " 23:59:59' "+
					"GROUP BY A.sg_uf_veiculo";
			
			pstmt = connect.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}	catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportVencidas: " + sqlExpt);
			return null;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportVencidas: " +  e);
			return null;
		}   finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap reportVencidasEstado(LocalDate dtInicial, LocalDate dtFinal, String sgUf) {
		return reportVencidasEstado(dtInicial, dtFinal, sgUf, null);
	}
	
	public static ResultSetMap reportVencidasEstado(LocalDate dtInicial, LocalDate dtFinal, String sgUf, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm;
			String sql = "SELECT A.id_ait, A.dt_infracao, A.dt_vencimento, A.vl_multa, B.tp_status, B.lg_enviado_detran "
					+ "FROM mob_ait A "
					+ "INNER JOIN mob_ait_movimento B ON B.cd_ait = A.cd_ait AND B.cd_movimento = A.cd_movimento_atual "
					+ "WHERE A.sg_uf_veiculo =? "
					+ "AND A.dt_vencimento IS NOT NULL "
					+ "AND B.tp_status !="+AitMovimentoServices.MULTA_PAGA+" AND B.tp_status != "+AitMovimentoServices.MULTA_PAGA_OUTRA_UF + " "
					+ "AND A.dt_vencimento BETWEEN '"+dtInicial+ "' AND '"+dtFinal+ " 23:59:59' "
					+ "ORDER BY A.dt_infracao DESC";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, sgUf);
			rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}	catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportVencidasEstado: " + sqlExpt);
			return null;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportVencidasEstado: " +  e);
			return null;
		}   finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap reportBancos (LocalDate dtInicial, LocalDate dtFinal) {
		return reportBancos(dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap reportBancos (LocalDate dtInicial, LocalDate dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm;
			String sql = "SELECT COUNT(A.cd_ait) quantidade, C.nr_banco, ROUND(CAST(SUM(A.vl_multa) AS decimal (16,2)), 2) AS vl_total " + 
					"FROM mob_ait A " + 
					"INNER JOIN mob_ait_movimento B ON B.cd_movimento = A.cd_movimento_atual AND B.cd_ait = A.cd_ait " +
					"INNER JOIN mob_ait_pagamento C ON C.cd_ait = A.cd_ait "+
					"WHERE B.tp_status ="+AitMovimentoServices.MULTA_PAGA+" "+
					"AND A.dt_infracao BETWEEN '"+dtInicial+ "' AND '"+dtFinal+ " 23:59:59' "+
					"GROUP BY C.nr_banco";
			
			pstmt = connect.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}	catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportBancos: " + sqlExpt);
			return null;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportBancos: " +  e);
			return null;
		}   finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap reportBancosDetalhamento (LocalDate dtInicial, LocalDate dtFinal, String nrBanco) {
		return reportBancosDetalhamento(dtInicial, dtFinal, nrBanco, null);
	}
	
	public static ResultSetMap reportBancosDetalhamento (LocalDate dtInicial, LocalDate dtFinal, String nrBanco, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm;
			String sql = "SELECT A.id_ait, A.dt_infracao, A.dt_vencimento, A.vl_multa, B.tp_status, B.lg_enviado_detran " + 
					"FROM mob_ait A " + 
					"INNER JOIN mob_ait_movimento B ON B.cd_movimento = A.cd_movimento_atual AND B.cd_ait = A.cd_ait " +
					"INNER JOIN mob_ait_pagamento C ON C.cd_ait = A.cd_ait "+
					"WHERE B.tp_status ="+AitMovimentoServices.MULTA_PAGA+" "+
					"AND A.dt_infracao BETWEEN '"+dtInicial+ "' AND '"+dtFinal+ " 23:59:59' "+
					(nrBanco.equals("null") ? "AND C.nr_banco IS NULL" : "AND C.nr_banco = '"+nrBanco+"' ");
			
			pstmt = connect.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}	catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportBancosDetalhamento: " + sqlExpt);
			return null;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoReportServices.reportBancosDetalhamento: " +  e);
			return null;
		}   finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
