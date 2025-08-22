package com.tivic.manager.mob.lote.impressao.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.CustomConnection;

public class CriarLoteNotificacaoValidations {
	private List<ValidatorNovoLoteNotificacao> validators ;
	
	public CriarLoteNotificacaoValidations(int tpRemessaCorreios, boolean isTask) throws Exception {
		this.validators = new ArrayList<ValidatorNovoLoteNotificacao>();
		this.validators.add(new QuantidadeImpressaoValidator(isTask));
		this.validators.add(new CorreiosEtiquetaValidator(tpRemessaCorreios));
	}
	
	public void validate(int qtdNotificacao, CustomConnection customConnection) throws Exception {
		for (ValidatorNovoLoteNotificacao validator: validators) {
			validator.validate(qtdNotificacao, customConnection);
		}
	}
}
