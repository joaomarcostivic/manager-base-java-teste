package com.tivic.manager.mob.convenio;

import java.util.List;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class MudaConvenioDefault implements IMudaConvenioDefault {
	
	private ConvenioRepository convenioRepository;
	
	public MudaConvenioDefault() throws Exception {
		convenioRepository = (ConvenioRepository) BeansFactory.get(ConvenioRepository.class);
	}

	@Override
	public void removerDefault(CustomConnection customConnection) throws Exception {
		List<Convenio> convenioList = convenioRepository.find(setCriteriosConvenioDefault(), customConnection);
		if(!convenioList.isEmpty()) {
			for(Convenio convenioDefault: convenioList) {				
				Convenio convenio = convenioDefault;
				convenio.setLgDefault(0);
				convenioRepository.update(convenio, customConnection);
			}
		}
	}
	
	private SearchCriterios setCriteriosConvenioDefault() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("lg_default", 1);
		return searchCriterios;
	}

	@Override
	public Convenio aplicarDefault(int cdConvenio, CustomConnection customConnection) throws Exception {
		Convenio convenio = convenioRepository.get(cdConvenio, customConnection);	
		convenio.setLgDefault(1);
		convenioRepository.update(convenio, customConnection);
		return convenio;
	}
}
