package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;

public class TipoOperacaoCfopServices {

	
	public static ResultSetMap getAll(int cdTipoOperacao) {
		return getAll(cdTipoOperacao, null);
	}

	public static ResultSetMap getAll(int cdTipoOperacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, BP.nm_pessoa AS nm_empresa, C.nm_estado, D.nm_ncm, E.nm_classificacao_fiscal, F.nm_natureza_operacao FROM adm_tipo_operacao_cfop A " +
											" LEFT OUTER JOIN grl_pessoa  BP ON (A.cd_empresa = BP.cd_pessoa) " +
											" LEFT OUTER JOIN grl_estado  C ON (A.cd_estado  = C.cd_estado) " +
											" LEFT OUTER JOIN grl_ncm     D ON (A.cd_ncm     = D.cd_ncm) " +
											" LEFT OUTER JOIN adm_classificacao_fiscal E ON (A.cd_classificacao_fiscal = E.cd_classificacao_fiscal) " +
											" JOIN adm_natureza_operacao F ON (A.cd_natureza_operacao = F.cd_natureza_operacao) " +
											" WHERE A.cd_tipo_operacao = " + cdTipoOperacao);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoCfopDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
