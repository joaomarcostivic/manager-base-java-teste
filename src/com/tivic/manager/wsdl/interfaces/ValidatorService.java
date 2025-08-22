package com.tivic.manager.wsdl.interfaces;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface ValidatorService {
	public String getMensagem();
	public void validate() throws ValidacaoException;
}
