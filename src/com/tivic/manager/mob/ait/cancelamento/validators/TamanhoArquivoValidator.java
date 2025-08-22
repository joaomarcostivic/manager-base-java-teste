package com.tivic.manager.mob.ait.cancelamento.validators;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.Arquivo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.validation.Validator;

public class TamanhoArquivoValidator implements Validator<Arquivo> {

	@Override
	public void validate(Arquivo arquivo, CustomConnection connection) throws ValidationException, Exception {
	    if (arquivo.getBlbArquivo().length > 20 * 1024 * 1024) {
	        throw new ValidationException("O tamanho do arquivo não pode ultrapassar 20 megabyte.");
	    }
	}
}
