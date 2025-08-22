package com.tivic.manager.mob.lote.impressao.publicacao.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.validation.Validator;

public class ConfirmaPublicacaoValidatios {

	private List<Validator<LoteImpressao>> validators;
	
	public ConfirmaPublicacaoValidatios() throws Exception {
		this.validators = new ArrayList<Validator<LoteImpressao>>();
		this.validators.add(new DataConfirmacaoValidator());
	}
	
	public void validate(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		for (Validator<LoteImpressao> validator: validators) {
			validator.validate(loteImpressao, customConnection);
		}
	}
	
}
