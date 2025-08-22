package com.tivic.manager.grl.equipamento.base;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.grl.equipamento.EquipamentoUsuario;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class EquipamentoServiceBaseAntiga implements EquipamentoServiceBase {

	@Override
	public List<EquipamentoUsuario> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<EquipamentoUsuario> search = new SearchBuilder<EquipamentoUsuario>("grl_equipamento")
				.fields("A.*, B.cod_usuario")
				.addJoinTable("LEFT OUTER JOIN usuario B ON (A.cd_equipamento = B.cd_equipamento)")
				.orderBy("A.nm_equipamento")
				.searchCriterios(searchCriterios)
				.build();
		return search.getList(EquipamentoUsuario.class);
	}

}
