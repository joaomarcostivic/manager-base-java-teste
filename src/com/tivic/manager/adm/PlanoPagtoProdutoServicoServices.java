package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class PlanoPagtoProdutoServicoServices	{
	public static ResultSetMap getProdutoServicoOfPlanoPagto(int cdPlanoPagamento) {
		return getProdutoServicoOfPlanoPagto(cdPlanoPagamento, null);
	}

	public static ResultSetMap getProdutoServicoOfPlanoPagto(int cdPlanoPagamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.nm_produto_servico, C.nm_tabela_preco, D.nm_grupo," +
					"       E.nm_tipo_operacao, F.nm_forma_pagamento " +
					"FROM adm_plano_pagto_produto_servico A " +
					"LEFT OUTER JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					"LEFT OUTER JOIN adm_tabela_preco    C ON (A.cd_tabela_preco = C.cd_tabela_preco) " +
					"LEFT OUTER JOIN alm_grupo           D ON (A.cd_grupo = D.cd_grupo) " +
					"LEFT OUTER JOIN adm_tipo_operacao   E ON (A.cd_tipo_operacao = E.cd_tipo_operacao) " +
					"LEFT OUTER JOIN adm_forma_pagamento F ON (A.cd_forma_pagamento = F.cd_forma_pagamento) " +
					"WHERE A.cd_plano_pagamento = "+cdPlanoPagamento+
					" ORDER BY nm_tipo_operacao, nm_forma_pagamento, nm_tabela_preco, D.nm_grupo, B.nm_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoServices.getProdutoServicoOfPlanoPagto: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findProdutoServico(ArrayList<ItemComparator> criterios, int cdPlanoPagamento) {
		return findProdutoServico(criterios, cdPlanoPagamento, null);
	}

	public static ResultSetMap findProdutoServico(ArrayList<ItemComparator> criterios, int cdPlanoPagamento, Connection connect) {
		return Search.find("SELECT * FROM grl_produto_servico A " +
						   "WHERE NOT A.cd_produto_servico IN (SELECT cd_produto_servico " +
						   "                                   FROM adm_plano_pagto_produto_servico B " +
						   "                                   WHERE B.cd_produto_servico = A.cd_produto_servico " +
						   "                                     AND B.cd_plano_pagamento = " + cdPlanoPagamento + ")", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}