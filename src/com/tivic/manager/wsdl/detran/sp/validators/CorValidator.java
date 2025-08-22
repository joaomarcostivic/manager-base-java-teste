package com.tivic.manager.wsdl.detran.sp.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ValidatorField;

public class CorValidator implements ValidatorField {
	
	private List<String> valoresPermitidos;
	
	private static final String AMARELA 	= "01";
	private static final String AZUL 		= "02";
	private static final String BEGE 		= "03";
	private static final String BRANCA		= "04";
	private static final String CINZA 		= "05";
	private static final String DOURADA		= "06";
	private static final String GRENA		= "07";
	private static final String LARANJA		= "08";
	private static final String MARROM		= "09";
	private static final String PRATA		= "10";
	private static final String PRETA		= "11";
	private static final String ROSA		= "12";
	private static final String ROXA		= "13";
	private static final String VERDE		= "14";
	private static final String VERMELHA	= "15";
	private static final String FANTASIA	= "16";
	private static final String VINHO		= "17";
	
	public CorValidator () {
		valoresPermitidos = new ArrayList<String>();
		valoresPermitidos.add(CorValidator.AMARELA);
		valoresPermitidos.add(CorValidator.AZUL);
		valoresPermitidos.add(CorValidator.BEGE);
		valoresPermitidos.add(CorValidator.BRANCA);
		valoresPermitidos.add(CorValidator.CINZA);
		valoresPermitidos.add(CorValidator.DOURADA);
		valoresPermitidos.add(CorValidator.GRENA);
		valoresPermitidos.add(CorValidator.LARANJA);
		valoresPermitidos.add(CorValidator.MARROM);
		valoresPermitidos.add(CorValidator.PRATA);
		valoresPermitidos.add(CorValidator.PRETA);
		valoresPermitidos.add(CorValidator.ROSA);
		valoresPermitidos.add(CorValidator.ROXA);
		valoresPermitidos.add(CorValidator.VERDE);
		valoresPermitidos.add(CorValidator.VERMELHA);
		valoresPermitidos.add(CorValidator.FANTASIA);
		valoresPermitidos.add(CorValidator.VINHO);
	}
		
	@Override
	public void validate(String valor, int tamanho) throws ValidacaoException {
		if(!valoresPermitidos.contains(valor)) {
			throw new ValidacaoException("Valor de cor inválido");
		}
	}
}