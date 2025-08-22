package com.tivic.manager.grl.equipamento;

import java.sql.Types;

import com.tivic.manager.rest.request.filter.Criterios;

import sol.dao.ItemComparator;

public class EquipamentoSearch {

	private Criterios criterios;
	
	public EquipamentoSearch(int limit,
		String idEquipamento, 
		int stEquipamento, 
		int tpEquipamento, 
		int cdOrgao) {
		criterios = new Criterios();
		if(idEquipamento != null)
			criterios.add("id_equipamento", idEquipamento, ItemComparator.LIKE_ANY, Types.VARCHAR);
		if(stEquipamento >= 0)
			criterios.add("st_equipamento", Integer.toString(stEquipamento), ItemComparator.EQUAL, Types.INTEGER);
		if(tpEquipamento >= 0)
			criterios.add("tp_equipamento", Integer.toString(tpEquipamento), ItemComparator.EQUAL, Types.INTEGER);
		if(cdOrgao > 0)
			criterios.add("cd_orgao", Integer.toString(cdOrgao), ItemComparator.EQUAL, Types.INTEGER);
	}
	
	public Criterios getCriterios() {
		return criterios;
	}
}
