package com.tivic.manager.wsdl.detran.mg.validators.condutor;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface ICondutorRegistroInfracaoValidator {
	public void validate(Ait ait, CustomConnection connection) throws Exception, ValidacaoException;
}
