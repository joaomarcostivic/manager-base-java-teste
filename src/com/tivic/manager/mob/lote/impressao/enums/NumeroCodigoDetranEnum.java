package com.tivic.manager.mob.lote.impressao.enums;

import com.tivic.manager.grl.ParametroServices;

public enum NumeroCodigoDetranEnum {

	PARADA_SOBRE_FAIXA(Integer.parseInt(ParametroServices.getValorOfParametro("NR_CODIGO_PARADA_SOBRE_FAIXA", 0)), "Parar sobre faixa de pedestres na mudança de sinal luminoso (fisc eletrônica)"),
	AVANCO_SINAL_VERMELHO(Integer.parseInt(ParametroServices.getValorOfParametro("NR_CODIGO_AVANCO_SINAL_VERMELHO", 0)), "Avançar o sinal vermelho do semáforo, exc houver sinaliz perm livre conv à direita - fisc eletrônica");

	private final Integer key;
	private final String value;

	NumeroCodigoDetranEnum(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static String valueOf(Integer key) {
		for (Integer index = 0; index < NumeroCodigoDetranEnum.values().length; index++) {
			if (key == NumeroCodigoDetranEnum.values()[index].getKey()) {
				return NumeroCodigoDetranEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
