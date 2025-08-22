package com.tivic.manager.str.validations;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.str.ProcessoDTO;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class SaveProcessoValidator {

	private ProcessoDTO processo;
	private List<Validator<ProcessoDTO>> validators;

	public SaveProcessoValidator(ProcessoDTO processo) {
		this.processo = processo;
		validators = new ArrayList<Validator<ProcessoDTO>>();
		validators.add(new DuplicidadeProcessoValidator());
	}

	public void validate(CustomConnection customConnection) throws Exception {
		for (Validator<ProcessoDTO> validator : validators) {
			validator.validate(processo,  customConnection);
		}
	}
	
}
