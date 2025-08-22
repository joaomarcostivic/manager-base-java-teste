package com.tivic.manager.mob.colaborador.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.colaborador.ColaboradorDTO;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class ColaboradorDesativacaoValidator {
	private ColaboradorDTO colaboradorDTO;
	private List<Validator<ColaboradorDTO>> validators;
	
	public ColaboradorDesativacaoValidator(ColaboradorDTO colaboradorDTO) {
		this.colaboradorDTO = colaboradorDTO;
		validators = new ArrayList<Validator<ColaboradorDTO>>();
		validators.add(new DataDesativacaoColaboradorValidator());
	}
	
	public void validate(CustomConnection customConnection) throws Exception {
		for(Validator<ColaboradorDTO> validator : validators) {
			validator.validate(colaboradorDTO, customConnection);
		}
	}
}
