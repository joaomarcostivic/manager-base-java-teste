package com.tivic.manager.wsdl.interfaces;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface ValidatorField {
	public void validate(String valor, int tamanho) throws ValidacaoException;
}
