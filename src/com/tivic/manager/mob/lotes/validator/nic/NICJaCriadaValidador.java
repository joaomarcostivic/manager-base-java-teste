package com.tivic.manager.mob.lotes.validator.nic;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class NICJaCriadaValidador  implements INICValidador {
	private AitRepository aitRepository;
	
	public NICJaCriadaValidador() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException {
		return naoExisteNic(ait.getCdAit());
	}
	
	private boolean naoExisteNic(int cdAit) throws Exception {
		List<Ait> aitList = this.aitRepository.find(montaSearchCriterios(cdAit));
		return aitList.isEmpty();
	}
	
	private SearchCriterios montaSearchCriterios(int cdAitOrigem) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait_origem", cdAitOrigem);
		return searchCriterios;
	}
	
}
