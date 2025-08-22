package com.tivic.manager.wsdl.exceptions;

import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ValidacaoSPException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServicoDetranObjeto servicoDetranObjeto;
	
	public ValidacaoSPException(String mensagem, ServicoDetranObjeto servicoDetranObjeto){
		super(mensagem);
		this.servicoDetranObjeto = servicoDetranObjeto;
	}
	
	
}
