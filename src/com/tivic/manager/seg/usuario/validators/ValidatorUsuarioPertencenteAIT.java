package com.tivic.manager.seg.usuario.validators;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ValidatorUsuarioPertencenteAIT implements Validator<Integer> {
	private AitRepository aitRepository;
	
	public ValidatorUsuarioPertencenteAIT() throws Exception {
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public void validate(Integer cdUsuario, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_usuario", cdUsuario);
		List<Ait> aits = aitRepository.find(searchCriterios);
		if(!aits.isEmpty()) {
			throw new BadRequestException("O usuário não pode ser excluído, pois está vinculado a AITs");
		}
	}
}
