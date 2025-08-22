package com.tivic.manager.mob.lotes.enums.correios;

public enum TipoArDigitalEnum {
	AR_DIGITAL(0, "Ar Digital"),
	AR_DIGITAL_2D(1, "Ar Digital 2D");
	
	private final Integer key;
	private final String value;
	
	TipoArDigitalEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoArDigitalEnum.values().length; index++) {
			if(key == TipoArDigitalEnum.values()[index].getKey()) {
				return TipoArDigitalEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
