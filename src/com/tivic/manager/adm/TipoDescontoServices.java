package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class TipoDescontoServices {

	public static ResultSetMap getAllFaixas(int cdTipoDesconto, int cdEmpresa) {
		return getAllFaixas(cdTipoDesconto, cdEmpresa, null);
	}

	public static ResultSetMap getAllFaixas(int cdTipoDesconto, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM adm_faixa_desconto " +
					"WHERE cd_tipo_desconto = ? " +
					"  AND cd_empresa = ?");
			pstmt.setInt(1, cdTipoDesconto);
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