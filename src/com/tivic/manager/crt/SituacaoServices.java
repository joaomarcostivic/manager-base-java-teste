package com.tivic.manager.crt;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class SituacaoServices {
	public static final int CONTRATO = 0;
	public static final int COMISSAO = 1;

	public static ResultSetMap getAllStContrato() {
		return Search.find("SELECT * FROM SCE_SITUACAO WHERE tp_situacao = 0", "ORDER BY nm_situacao", new ArrayList<ItemComparator>(), Conexao.conectar(), true);
	}

	public static ResultSetMap getAllStComissao() {
		return Search.find("SELECT * FROM SCE_SITUACAO WHERE tp_situacao = 1", "ORDER BY nm_situacao", new ArrayList<ItemComparator>(), Conexao.conectar(), true);
	}

	public static ResultSetMap findStContrato(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_SITUACAO WHERE tp_situacao = 0", "ORDER BY nm_situacao", criterios, Conexao.conectar(), true);
	}

	public static ResultSetMap findStComissao(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_SITUACAO WHERE tp_situacao = 1", "ORDER BY nm_situacao", criterios, Conexao.conectar(), true);
	}
}