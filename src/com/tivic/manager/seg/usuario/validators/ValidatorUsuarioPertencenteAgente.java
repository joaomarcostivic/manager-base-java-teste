package com.tivic.manager.seg.usuario.validators;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.agente.AgenteRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ValidatorUsuarioPertencenteAgente implements Validator<Integer> {
	private AgenteRepository agenteRepository;
	
	public ValidatorUsuarioPertencenteAgente() throws Exception {
		agenteRepository = (AgenteRepository) BeansFactory.get(AgenteRepository.class);
	}

	@Override
	public void validate(Integer cdUsuario, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_usuario", cdUsuario);
		List<Agente> agentes = agenteRepository.find(searchCriterios);
		if(!agentes.isEmpty()) {
			throw new BadRequestException("O usuário não pode ser excluído, pois está vinculado a agentes.");
		}
	}

}
