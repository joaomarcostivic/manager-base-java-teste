package com.tivic.manager.srh;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class TipoMovimentacaoServices {

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.id_evento_financeiro, B.nm_evento_financeiro " +
				           "FROM srh_tipo_movimentacao A " +
				           "LEFT OUTER JOIN adm_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro)", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}