package com.tivic.manager.wsdl.detran.sp.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ValidatorField;

public class AutoEntregueValidator implements ValidatorField {
	
	private List<String> valoresPermitidos;
	
	private static final String NAO_INFORMADO	= "9";
	private static final String NAO_ASSINADO	= "10";
	private static final String ASSINADO		= "11";
	private static final String NAO_ENTREGUE	= "12";
	
	public AutoEntregueValidator () {
		valoresPermitidos = new ArrayList<String>();
		valoresPermitidos.add(AutoEntregueValidator.NAO_INFORMADO);
		valoresPermitidos.add(AutoEntregueValidator.NAO_ASSINADO);
		valoresPermitidos.add(AutoEntregueValidator.ASSINADO);
		valoresPermitidos.add(AutoEntregueValidator.NAO_ENTREGUE);
	}
		
	@Override
	public void validate(String valor, int tamanho) throws ValidacaoException {
		if(!valoresPermitidos.contains(valor)) {
			throw new ValidacaoException("Valor de auto entregue inválido");
		}
	}
}