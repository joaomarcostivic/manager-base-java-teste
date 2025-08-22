package com.tivic.manager.mob.ait.cancelamento.validators;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.Arquivo;
import com.tivic.sol.validation.Validator;
import com.tivic.sol.connection.CustomConnection;

public class ArquivoIsNullValidator implements Validator<Arquivo>{

	@Override
	public void validate(Arquivo arquivo, CustomConnection connection) throws ValidationException, Exception {
	    if (arquivo.getBlbArquivo() == null || arquivo.getBlbArquivo().length == 0) {
	        throw new ValidationException("O arquivo está nulo.");
	    }
	}

}
