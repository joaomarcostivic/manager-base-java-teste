package com.tivic.manager.ptc.protocolosv3.enums;

public enum CodigoPaisCNHEnum {	
	ARGENTINA(10, "Argentina"),
	BOLIVIA(11, "Bolívia"),
	GUIANA(20, "Guiana"),
	CHILE(30, "Chile"),
	VENEZUELA(40, "Venezuela"),
	PARAGUAI(60, "Paraguai"),
	URUGUAI(80, "Uruguai"),
	MEXICO(90, "México"),
	ESTADOS_UNIDOS_DA_AMERICA(91, "Estados Unidos da América"),
	CANADA(92, "Canada"),
	OUTROS(99, "Outros");
	
	private final Integer key;
	private final String value;
	
	CodigoPaisCNHEnum(Integer key, String value){
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
		for(Integer index=0; index < CodigoPaisCNHEnum.values().length; index++) {
			if(key == CodigoPaisCNHEnum.values()[index].getKey()) {
				return CodigoPaisCNHEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
