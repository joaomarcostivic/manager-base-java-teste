package com.tivic.manager.seg.usuario.validators;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ValidatorUsuarioPertencenteMovimento implements Validator<Integer> {
	private AitMovimentoRepository aitMovimentoRepository;
	
	public ValidatorUsuarioPertencenteMovimento() throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}

	@Override
	public void validate(Integer cdUsuario, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_usuario", cdUsuario);
		List<AitMovimento> movimentoPorUsuario = aitMovimentoRepository.find(searchCriterios);
		if(!movimentoPorUsuario.isEmpty()) {
			throw new BadRequestException("O usuário não pode ser excluído, pois está vinculado a movimentos.");
		}
	}

}
