package com.tivic.manager.mob.lotes.validator;

import com.tivic.sol.connection.CustomConnection;

public class QuantidadeImpressaoValidator implements IValidatorNovoLoteNotificacao{

	@Override
	public void validate(int qtdNotificacao, CustomConnection customConnection) throws Exception {
		if (qtdNotificacao > 1000) {
			throw new Exception("Só é possível gerar lote com no maximo 1000 AITs!");
		}
	}
}