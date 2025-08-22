package com.tivic.manager.mob.lotes.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.sol.connection.CustomConnection;

public class LoteNotificacaoValidations {
private List<IValidator<Ait>> validators;
	
	public LoteNotificacaoValidations() throws Exception {
		this.validators = new ArrayList<IValidator<Ait>>();
		this.validators.add(new NomeProprietarioValidation());
		this.validators.add(new SituacaoAitValidator());
		this.validators.add(new NumeroControleValidator());
		this.validators.add(new CpfCnpjProprietarioValidator());
		this.validators.add(new ModeloVeiculoValidator());
		this.validators.add(new EspecieVeiculoValidator());
		this.validators.add(new NumeroRenavamValidator());
		this.validators.add(new CancelamentoValidator());
	}
	
	public void validate(Ait ait, CustomConnection customConnection) throws Exception {
		for (IValidator<Ait> validator: validators) {
			validator.validate(ait, customConnection);
		}
	}
}
