package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class PlanoParcelamentoServices {

	public static ResultSetMap getParcelamentoOfPlanoPagamento(int cdPlanoPagamento) {
		return getParcelamentoOfPlanoPagamento(cdPlanoPagamento, null);
	}

	public static ResultSetMap getParcelamentoOfPlanoPagamento(int cdPlanoPagamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
															   "FROM adm_plano_parcelamento A " +
															   "WHERE A.cd_plano_pagamento = "+cdPlanoPagamento+
															   " ORDER BY nr_dias");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoServices.getParcelamentoOfPlanoPagamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}