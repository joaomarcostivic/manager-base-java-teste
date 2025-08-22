package com.tivic.manager.wsdl.detran.mg.validators;


import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ValidatorField;

public class OrgaoValidator  implements ValidatorField{

	public OrgaoValidator() {
		
	}

	@Override
	public void validate(String valor, int tamanho) throws ValidacaoException {
		if(valor.length() > tamanho){
			throw new ValidacaoException("O Valor de orgao deve ser menor do que " + String.valueOf(tamanho));
		}
	}

}
