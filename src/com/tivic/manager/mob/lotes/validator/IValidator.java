package com.tivic.manager.mob.lotes.validator;

import com.tivic.sol.connection.CustomConnection;

public interface IValidator<T> {
	public void validate(T object, CustomConnection customConnection) throws Exception;
}
