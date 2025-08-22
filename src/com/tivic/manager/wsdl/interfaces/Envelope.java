package com.tivic.manager.wsdl.interfaces;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface Envelope {
	public Object enviar() throws InstantiationException, IllegalAccessException, ValidacaoException;
	public void validate() throws ValidacaoException;
}
