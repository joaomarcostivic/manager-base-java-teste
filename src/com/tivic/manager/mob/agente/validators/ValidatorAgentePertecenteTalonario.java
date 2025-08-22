package com.tivic.manager.mob.agente.validators;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.talonario.TalonarioRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ValidatorAgentePertecenteTalonario implements Validator<Integer> {
	private TalonarioRepository talonarioRepository;
	
	public ValidatorAgentePertecenteTalonario() throws Exception {
		talonarioRepository = (TalonarioRepository) BeansFactory.get(TalonarioRepository.class);
	}

	@Override
	public void validate(Integer cdAgente, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_agente", cdAgente);
		List<Talonario> talonarios = talonarioRepository.find(searchCriterios, customConnection);
		if(!talonarios.isEmpty()) {
			throw new BadRequestException("O agente não pode ser excluído, pois está vinculado a talonários.");
		}
	}
}
