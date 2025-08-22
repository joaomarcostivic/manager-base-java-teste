package com.tivic.manager.ctb;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class LancamentoAutoServices {
	/* Situações (st_lancamento_auto) */
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;

	public static final String[] situacaoLancamentoAuto = {"Inativo", "Ativo"};

	public static ResultSetMap findCompleto() {
		return findCompleto(null, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, A.cd_lancamento_auto || ' - ' || A.nm_lancamento_auto AS ds_lancamento_auto, " +
						   "	B.nm_conta AS nm_conta_debito, " +
						   "	C.nm_conta AS nm_conta_credito, " +
						   "	E.nr_centro_custo || ' - ' || E.nm_centro_custo AS ds_centro_custo_debito, " +
						   "	F.nr_centro_custo || ' - ' || F.nm_centro_custo AS ds_centro_custo_credito, " +
						   " 	D.nm_historico, D.lg_complemento " +
						   "FROM ctb_conta B, ctb_conta C, ctb_lancamento_auto A " +
						   "	LEFT OUTER JOIN ctb_historico D ON (A.cd_historico = D.cd_historico) " +
						   "	LEFT OUTER JOIN ctb_centro_custo E ON (A.cd_centro_custo_debito = E.cd_centro_custo) " +
						   "	LEFT OUTER JOIN ctb_centro_custo F ON (A.cd_centro_custo_credito = F.cd_centro_custo) " +
						   "WHERE (A.cd_conta_debito = B.cd_conta) " +
						   "    AND (A.cd_conta_credito = C.cd_conta) ", "", criterios, null, connect!=null ? connect : Conexao.conectar(), connect==null, false, false);
	}
}
