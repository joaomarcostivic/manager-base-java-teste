package com.tivic.manager.auth.mobile.validator;

import com.tivic.manager.auth.mobile.AuthMobile;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IAuthMobileValidator {
	public void validate(AuthMobile auth, Equipamento equipamento) throws Exception, ValidacaoException;
}
