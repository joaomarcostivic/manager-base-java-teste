package com.tivic.manager.mob.lote.impressao;

public enum PrazosNIPEnum {
	INICIO_VIGENCIA_PRAZO(0, "2021-10-21"),
	PRAZO_180_DIAS(180, "Prazo de 180 para emissão da NIP caso não tenha defesa"),
	PRAZO_360_DIAS(360, "Prazo de 360 para emissão da NIP caso tenha defesa" );
	
	private final Integer key;
	private final String value;
	
	PrazosNIPEnum(Integer key, String value){
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
		for(Integer index=0; index < PrazosNIPEnum.values().length; index++) {
			if(key == PrazosNIPEnum.values()[index].getKey()) {
				return PrazosNIPEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
