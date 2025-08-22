package com.tivic.manager.auth.mobile.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.auth.mobile.AuthMobile;
import com.tivic.manager.grl.equipamento.Equipamento;

public class AuthMobileValidatorBuilder {
	List<IAuthMobileValidator> validators;
	
	public AuthMobileValidatorBuilder() throws Exception {
		validators = new ArrayList<IAuthMobileValidator>();
		validators.add(new EquipamentoStatusValidator());
		validators.add(new UsuarioPossuiAgenteValidator());
		validators.add(new UsuarioPossuiEquipamentoValidator());
		validators.add(new EquipamentoPossuiUsuarioValidator());
	}
	
	public void validate(AuthMobile auth, Equipamento equipamento) throws Exception {
		for(IAuthMobileValidator validator: this.validators) {
			validator.validate(auth, equipamento);
		}
	}
}
