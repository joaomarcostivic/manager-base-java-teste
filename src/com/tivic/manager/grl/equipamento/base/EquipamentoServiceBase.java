package com.tivic.manager.grl.equipamento.base;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.grl.equipamento.EquipamentoUsuario;
import com.tivic.sol.search.SearchCriterios;

public interface EquipamentoServiceBase {
	public List<EquipamentoUsuario> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
