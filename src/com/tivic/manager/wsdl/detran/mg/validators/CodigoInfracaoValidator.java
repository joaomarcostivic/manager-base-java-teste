package com.tivic.manager.wsdl.detran.mg.validators;

import com.tivic.manager.mob.Infracao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.validation.Validator;

public class CodigoInfracaoValidator implements Validator<Infracao> {
	
	@Override
	public void validate(Infracao infracao, CustomConnection customConnection) throws ValidacaoException {
		if(infracao.getDtFimVigencia() != null) {
			throw new ValidacaoException("Infração fora de vigência.");
		}
	}
}
