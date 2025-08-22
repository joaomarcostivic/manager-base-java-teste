package com.tivic.manager.wsdl.detran.mg.validators;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.validation.Validator;

public class IncluirModeloCnhValidator implements Validator<ApresentacaoCondutor>{

	@Override
	public void validate(ApresentacaoCondutor documento, CustomConnection conn) throws Exception, ValidacaoException {
		if(documento.getTpModeloCnh() == 0 ) {
			throw new ValidacaoException("Modelo da CNH inválido.");
		}
	}

}
