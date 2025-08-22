package com.tivic.manager.grl.parametro.enums;

public enum TipoDadoParametroEnum {
	NUMERICO(0, "Integer"),
	STRING(1, "String"),
	BYTE(2, "Byte");
	
	private final Integer key;
	private final String value;
	
	TipoDadoParametroEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoDadoParametroEnum.values().length; index++) {
			if(key == TipoDadoParametroEnum.values()[index].getKey()) {
				return TipoDadoParametroEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
