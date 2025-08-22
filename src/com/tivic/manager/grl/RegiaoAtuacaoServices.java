package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class RegiaoAtuacaoServices {
	public static ResultSetMap find(int cdPessoa, int cdEmpresa) {
		return find(cdPessoa, cdEmpresa, null);
	}

	public static ResultSetMap find(int cdPessoa, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM grl_regiao B, " +
					"  grl_vinculo C, " +
					"  grl_regiao_atuacao A " +
					"     LEFT OUTER JOIN adm_conta_carteira D " +
					"       ON (A.cd_conta_carteira = D.cd_conta_carteira " +
					"       AND A.cd_conta = D.cd_conta) " +
					"WHERE A.cd_regiao = B.cd_regiao " +
					"  AND A.cd_vinculo = C.cd_vinculo " +
					"  AND A.cd_pessoa = ? " +
					"  AND A.cd_empresa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdEmpresa);

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
