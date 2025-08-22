package com.tivic.manager.mob.agente.validators;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ValidatorAgentePertecenteAit implements Validator<Integer> {
	private AitRepository aitRepository;
	
	public ValidatorAgentePertecenteAit() throws Exception {
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public void validate(Integer cdAgente, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_agente", cdAgente);
		List<Ait> aitList = aitRepository.find(searchCriterios);
		if(!aitList.isEmpty()) {
			throw new BadRequestException("O agente não pode ser excluído, pois está vinculado a AITs.");
		}
	}
}
