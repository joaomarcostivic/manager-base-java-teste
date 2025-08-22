package com.tivic.manager.mob.lote.impressao.validator;

import com.tivic.sol.connection.CustomConnection;

public interface ValidatorNovoLoteNotificacao {
	void validate(int qtdNotificacao, CustomConnection customConnection) throws Exception;
}
