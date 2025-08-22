package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;


public class CboPessoaFisicaServices {

	public static int setCboPrincipal(int cdPessoa, int cdCbo) {
		return setCboPrincipal(cdPessoa, cdCbo, null);
	}

	public static int setCboPrincipal(int cdPessoa, int cdCbo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PreparedStatement pstmt = connection.prepareStatement("UPDATE grl_cbo_pessoa_fisica " +
					"SET lg_principal = 0 " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			pstmt = connection.prepareStatement("UPDATE grl_cbo_pessoa_fisica " +
					"SET lg_principal = 1 " +
					"WHERE cd_pessoa = ? " +
					"  AND cd_cbo = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdCbo);
			pstmt.execute();

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
