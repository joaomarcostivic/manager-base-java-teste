package com.tivic.manager.mob.processamento.sincronizacao.factories;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.mob.processamento.sincronizacao.OrgaoQuerySelector;
import com.tivic.sol.search.SearchCriterios;

public class CriterioFactory {
	private SearchCriterios searchCriterios;
	
	public CriterioFactory(SearchCriterios searchCriterios) {
		this.searchCriterios = searchCriterios;
	}
	
	public ICriterioStrategy getStrategy() {
		String cdOrgaoAutuador = ManagerConf.getInstance().get("RADAR_CD_ORGAO_AUTUADOR");
    	boolean isSerial = new OrgaoQuerySelector().isSerialId( cdOrgaoAutuador);
		return  isSerial ? new  CriterioSerialStrategy(this.searchCriterios) : 
						   new CriterioEquipamentoStrategy(searchCriterios);
	}
}
