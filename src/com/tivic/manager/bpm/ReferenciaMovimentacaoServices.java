package com.tivic.manager.bpm;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ReferenciaMovimentacaoServices {

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		ResultSetMap rsm = Search.find("SELECT A.*, D.nm_produto_servico, D.id_produto_servico, B.dt_aquisicao, B.dt_garantia, " +
				"B.dt_validade, B.dt_baixa, B.nr_serie, B.nr_tombo, C.nm_setor, " +
				"(SELECT MAX(F.nm_setor) FROM bpm_referencia_movimentacao E, grl_setor F " +
				"  WHERE E.cd_setor = F.cd_setor " +
				"    AND A.cd_referencia = E.cd_referencia " +
				"    AND E.dt_movimentacao = (SELECT MAX(G.dt_movimentacao) FROM bpm_referencia_movimentacao G " +
				"							  WHERE E.cd_referencia = G.cd_referencia " +
				"								AND G.dt_movimentacao < A.dt_movimentacao)) AS nm_setor_origem " +
				"FROM bpm_referencia_movimentacao A, bpm_referencia B, grl_setor C, grl_produto_servico D " +
				"WHERE A.cd_referencia = B.cd_referencia " +
				"  AND A.cd_setor = C.cd_setor " +
				"  AND B.cd_produto_servico = D.cd_produto_servico " +
				"  AND EXISTS (SELECT H.cd_movimentacao FROM bpm_referencia_movimentacao H " +
				"			   WHERE H.cd_referencia = A.cd_referencia " +
				"				 AND H.dt_movimentacao < A.dt_movimentacao)", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		ArrayList<String> criteriosOrder = new ArrayList<String>();
		criteriosOrder.add("dt_movimentacao DESC");
		rsm.orderBy(criteriosOrder);
		return rsm;
	}

}
