package com.tivic.manager.mob.lotes.validator;

import com.tivic.sol.connection.CustomConnection;

public interface IValidatorNovoLoteNotificacao {
	void validate(int qtdNotificacao, CustomConnection customConnection) throws Exception;

}
