package com.tivic.manager.mob.lotes.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.CustomConnection;

public class CriarLoteNotificacaoValidations {
private List<IValidatorNovoLoteNotificacao> validators ;
	
	public CriarLoteNotificacaoValidations(int tpRemessaCorreios) throws Exception {
		this.validators = new ArrayList<IValidatorNovoLoteNotificacao>();
		this.validators.add(new QuantidadeImpressaoValidator());
		this.validators.add(new CorreiosEtiquetaValidator(tpRemessaCorreios));
	}
	
	public void validate(int qtdNotificacao, CustomConnection customConnection) throws Exception {
		for (IValidatorNovoLoteNotificacao validator: validators) {
			validator.validate(qtdNotificacao, customConnection);
		}
	}
}
