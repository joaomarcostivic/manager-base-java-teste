package com.tivic.manager.ptc.protocolos.validators;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;

public interface IValidator <T>{
	
	public void validate(T obj, CustomConnection connection) throws ValidationException, Exception;
}
