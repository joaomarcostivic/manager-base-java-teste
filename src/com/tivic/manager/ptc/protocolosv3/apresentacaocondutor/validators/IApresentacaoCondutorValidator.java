package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators;

import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IApresentacaoCondutorValidator {
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection connection) throws Exception, ValidacaoException;

}
