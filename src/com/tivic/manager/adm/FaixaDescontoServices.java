package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class FaixaDescontoServices {

	public static ResultSetMap getAllRegras(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto) {
		return getAllRegras(cdTipoDesconto, cdEmpresa, cdFaixaDesconto, null);
	}

	public static ResultSetMap getAllRegras(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_produto_servico, C.nm_plano_pagamento " +
					"FROM adm_faixa_desconto_regra A " +
					"LEFT OUTER JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					"LEFT OUTER JOIN adm_plano_pagamento C ON (A.cd_plano_pagamento = C.cd_plano_pagamento) " +
					"WHERE A.cd_tipo_desconto = ? " +
					"  AND A.cd_empresa = ? " +
					"  AND A.cd_faixa_desconto = ?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdFaixaDesconto);
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

	public static int delete(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto) {
		return delete(cdTipoDesconto, cdEmpresa, cdFaixaDesconto, null);
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PreparedStatement pstmt = connect.prepareStatement("DELETE " +
					"FROM adm_faixa_desconto_regra " +
					"WHERE cd_tipo_desconto = ? " +
					"  AND cd_empresa = ? " +
					"  AND cd_faixa_desconto = ?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdFaixaDesconto);
			pstmt.execute();

			if (FaixaDescontoDAO.delete(cdTipoDesconto, cdEmpresa, cdFaixaDesconto, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
