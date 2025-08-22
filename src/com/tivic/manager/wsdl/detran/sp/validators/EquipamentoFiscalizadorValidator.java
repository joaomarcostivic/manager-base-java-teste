package com.tivic.manager.wsdl.detran.sp.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ValidatorField;

public class EquipamentoFiscalizadorValidator implements ValidatorField {
	
	private List<String> valoresPermitidos;
	
	private static final String RADAR 			= "1";
	private static final String DECIBELIMETRO	= "2";
	private static final String ETILOMETRO 		= "3";
	
	public EquipamentoFiscalizadorValidator () {
		valoresPermitidos = new ArrayList<String>();
		valoresPermitidos.add(EquipamentoFiscalizadorValidator.RADAR);
		valoresPermitidos.add(EquipamentoFiscalizadorValidator.DECIBELIMETRO);
		valoresPermitidos.add(EquipamentoFiscalizadorValidator.ETILOMETRO);
	}
		
	@Override
	public void validate(String valor, int tamanho) throws ValidacaoException {
		if(!valoresPermitidos.contains(valor)) {
			throw new ValidacaoException("Valor de equipamento fiscalizador inválido");
		}
	}
}