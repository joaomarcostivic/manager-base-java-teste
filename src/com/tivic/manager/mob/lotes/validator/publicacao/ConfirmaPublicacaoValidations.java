package com.tivic.manager.mob.lotes.validator.publicacao;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.validation.Validator;

public class ConfirmaPublicacaoValidations {
	private List<Validator<LotePublicacao>> validators;
	
	public ConfirmaPublicacaoValidations() throws Exception {
		this.validators = new ArrayList<Validator<LotePublicacao>>();
		this.validators.add(new DataConfirmacaoValidator());
	}
	
	public void validate(LotePublicacao lotePublicacao, CustomConnection customConnection) throws Exception {
		for (Validator<LotePublicacao> validator: validators) {
			validator.validate(lotePublicacao, customConnection);
		}
	}
	
}
