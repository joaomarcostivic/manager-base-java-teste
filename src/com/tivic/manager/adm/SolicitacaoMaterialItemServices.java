package com.tivic.manager.adm;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class SolicitacaoMaterialItemServices {

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, " +
					   	   " 	   B.dt_solicitacao, B.st_solicitacao_material, B.id_solicitacao_material, " +
						   "	   C.sg_unidade_medida, " +
						   "       D.nm_setor " +
				           "FROM adm_solicitacao_material_item A " +
				           "JOIN adm_solicitacao_material B ON (A.cd_solicitacao_material = B.cd_solicitacao_material) " +
  						   "LEFT OUTER JOIN grl_unidade_medida C ON (A.cd_unidade_medida = C.cd_unidade_medida) " +
						   "LEFT OUTER JOIN grl_setor D ON (B.cd_setor_solicitante = D.cd_setor) ", 
				           criterios, Conexao.conectar());
	}

}
