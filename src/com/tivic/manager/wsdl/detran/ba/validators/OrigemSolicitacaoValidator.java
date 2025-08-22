package com.tivic.manager.wsdl.detran.ba.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ValidatorField;

public class OrigemSolicitacaoValidator implements ValidatorField{

	private List<String> valoresPermitidos;
	
	public OrigemSolicitacaoValidator() {
		valoresPermitidos = new ArrayList<String>();
		valoresPermitidos.add("1");
		valoresPermitidos.add("2");
		valoresPermitidos.add("3");
		valoresPermitidos.add("4");
	}

	@Override
	public void validate(String valor, int tamanho) throws ValidacaoException {
		if(!valoresPermitidos.contains(valor)){
			throw new ValidacaoException("O Valor de origem_solicitacao deve estar entre 1, 2, 3 e 4");
		}
	}
}
