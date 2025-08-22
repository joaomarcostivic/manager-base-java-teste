package com.tivic.manager.mob.lote.impressao.validator;

import com.tivic.sol.connection.CustomConnection;

public class QuantidadeImpressaoValidator implements ValidatorNovoLoteNotificacao{
	private boolean isTask;
	
	public QuantidadeImpressaoValidator(boolean isTask) {
		this.isTask = isTask;
	}

	@Override
	public void validate(int qtdNotificacao, CustomConnection customConnection) throws Exception {
		if (!isTask && qtdNotificacao > 1000) {
			throw new Exception("Só é possível gerar lote com no maximo 1000 AITs!");
		}
		if (qtdNotificacao == 0) {
			throw new Exception("Não é possível gerar o lote: todos os AITs possuem inconsistências.");
		}
	}
}
