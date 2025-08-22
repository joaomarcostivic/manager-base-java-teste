package com.tivic.manager.mob.colaborador.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.colaborador.ColaboradorDTO;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class ColaboradorValidator {
	private ColaboradorDTO colaboradorDTO;
	private List<Validator<ColaboradorDTO>> validators;
	
	public ColaboradorValidator(ColaboradorDTO colaboradorDTO) throws Exception {
		this.colaboradorDTO = colaboradorDTO;
		validators = new ArrayList<Validator<ColaboradorDTO>>();
		validators.add(new ColaboradorPorVinculoValidator());
	}
	
	public void validate(CustomConnection customConnection) throws Exception {
		for(Validator<ColaboradorDTO> validator : validators) {
			validator.validate(colaboradorDTO, customConnection);
		}
	}

}
