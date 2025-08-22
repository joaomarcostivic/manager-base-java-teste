package com.tivic.manager.wsdl.detran.mg.validators.condutor;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class ModeloCnhValidator implements ICondutorRegistroInfracaoValidator {

	@Override
	public void validate(Ait ait, CustomConnection connection) throws Exception, ValidacaoException {
		if(ait.getTpCnhCondutor() <= 0 && (existsNrCnh(ait) || existsUfCnh(ait))) {
			throw new ValidacaoException("O modelo da CNH deve ser informado.");
		}
	}
	
	private boolean existsNrCnh(Ait ait) {
		return ait.getNrCnhCondutor() != null && !ait.getNrCnhCondutor().trim().equals("");
	}
	
	private boolean existsUfCnh(Ait ait) {
		return ait.getUfCnhCondutor() != null && !ait.getUfCnhCondutor().trim().equals("");
	}
}
