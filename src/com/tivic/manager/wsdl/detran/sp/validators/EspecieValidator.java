package com.tivic.manager.wsdl.detran.sp.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ValidatorField;

public class EspecieValidator implements ValidatorField {
	
	private List<String> valoresPermitidos;
	
	private static final String PASSAGEIRO 	= "01";
	private static final String CARGA 		= "02";
	private static final String MISTO 		= "03";
	private static final String CORRIDA		= "04";
	private static final String TRACAO 		= "05";
	private static final String ESPECIAL	= "06";
	private static final String COLECAO		= "07";
	
	public EspecieValidator () {
		valoresPermitidos = new ArrayList<String>();
		valoresPermitidos.add(EspecieValidator.PASSAGEIRO);
		valoresPermitidos.add(EspecieValidator.CARGA);
		valoresPermitidos.add(EspecieValidator.MISTO);
		valoresPermitidos.add(EspecieValidator.CORRIDA);
		valoresPermitidos.add(EspecieValidator.TRACAO);
		valoresPermitidos.add(EspecieValidator.ESPECIAL);
		valoresPermitidos.add(EspecieValidator.COLECAO);
	}
		
	@Override
	public void validate(String valor, int tamanho) throws ValidacaoException {
		if(!valoresPermitidos.contains(valor)) {
			throw new ValidacaoException("Valor de espécie inválido");
		}
	}
}