package com.tivic.manager.seg.usuario.validators;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ValidatorUsuarioPertencenteLoteImpressao implements Validator<Integer> {
	private ILoteImpressaoRepository loteImpressaoRepository;
	
	public ValidatorUsuarioPertencenteLoteImpressao() throws Exception {
		loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
	}

	@Override
	public void validate(Integer cdUsuario, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_usuario", cdUsuario);
		List<LoteImpressao> lotes = loteImpressaoRepository.findLoteImpressao(searchCriterios, customConnection);
		if(!lotes.isEmpty()) {
			throw new BadRequestException("O usuário não pode ser excluído, pois está vinculado a lotes de AIT.");
		}
	}

}
