package com.tivic.manager.mob.lote.impressao.validator;

import java.util.ArrayList;
import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.util.validator.Validator;

public class LoteNotificacaoValidations {
	private List<Validator<Ait>> validators;
	
	public LoteNotificacaoValidations() throws Exception {
		this.validators = new ArrayList<Validator<Ait>>();
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
		for (Validator<Ait> validator: validators) {
			validator.validate(ait, customConnection);
		}
	}
}
