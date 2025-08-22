package com.tivic.manager.bpm;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class BemServices {

	public static final String[] tipoManutencao = {"Corretiva", "Preventiva", "Preditiva", "Produtiva Total"};

	public static final int TP_MANUTENCAO_CORRETIVA = 0;
	public static final int TP_MANUTENCAO_PREVENTIVA = 1;
	public static final int TP_MANUTENCAO_PREDITIVA = 2;
	public static final int TP_MANUTENCAO_PRODUTIVA_TOTAL = 3;

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		int cdEmpresa = 0;

		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().toUpperCase().indexOf("CD_EMPRESA") != -1) {
				cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
		}

		String sql = "SELECT A.*, " +
			"B.cd_categoria_economica, B.nm_produto_servico, B.txt_produto_servico, B.txt_especificacao, " +
			"B.txt_dado_tecnico, B.txt_prazo_entrega, B.tp_produto_servico, B.id_produto_servico, " +
			"B.sg_produto_servico, B.cd_classificacao_fiscal, B.cd_fabricante, " +
			"C.nm_pessoa AS nm_fabricante, " +
		    "D.id_classificacao_fiscal, D.nm_classificacao_fiscal, " +
		    "E.nm_classificacao, H.cd_grupo, H. lg_principal, I.nm_grupo " +
		    "FROM bpm_bem A " +
		    "LEFT OUTER JOIN grl_produto_servico B ON (A.cd_bem = B.cd_produto_servico) " +
 		    "LEFT OUTER JOIN grl_produto_servico_empresa F ON (B.cd_produto_servico = F.cd_produto_servico AND F.cd_empresa = " + cdEmpresa + ") " +
		    "LEFT OUTER JOIN grl_pessoa C ON (B.cd_fabricante = C.cd_pessoa) " +
		    "LEFT OUTER JOIN adm_classificacao_fiscal D ON (B.cd_classificacao_fiscal = D.cd_classificacao_fiscal) " +
		    "LEFT OUTER JOIN bpm_classificacao E ON (A.cd_classificacao = E.cd_classificacao) " +
			"LEFT OUTER JOIN alm_produto_grupo H ON (B.cd_produto_servico = H.cd_produto_servico AND H.cd_empresa = " + cdEmpresa + ") " +
			"LEFT OUTER JOIN alm_grupo I ON (H.cd_grupo = I.cd_grupo) ";
		ResultSetMap rsm = Search.find(sql, "", criterios, null, connect!=null ? connect : Conexao.conectar(), true, false, false);
		return rsm;
	}
}