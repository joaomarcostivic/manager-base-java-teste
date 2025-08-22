package com.tivic.manager.util.validator;

import com.tivic.sol.connection.CustomConnection;

public interface Validator<T> {
	public void validate(T object, CustomConnection customConnection) throws Exception;
}
