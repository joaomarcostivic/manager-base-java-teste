package com.tivic.manager.flp;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class MatriculaEventoFinanceiroServices 	{

	public static ResultSetMap find(int cdMatricula) {
		return find(cdMatricula, null);
	}

	public static ResultSetMap find(int cdMatricula, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT a.dt_inicio, a.qt_repeticoes, " +
																  " a.qt_evento_financeiro, " +
																  " a.vl_evento_financeiro as vl_evento_financeiro_matricula, " +
																  " a.qt_horas, b.*, c.* " +
					                                              " FROM flp_matricula_evento_financeiro a, " +
					                                              " flp_evento_financeiro b, adm_evento_financeiro c " +
					                                              " WHERE a.cd_evento_financeiro = b.cd_evento_financeiro " +
					                                              "   AND b.cd_evento_financeiro = c.cd_evento_financeiro " +
					                                              "   AND a.cd_matricula = ?");
			pstmt.setInt(1, cdMatricula);

			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}