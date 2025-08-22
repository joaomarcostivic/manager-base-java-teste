package com.tivic.manager.ptc.protocolosv3.enums;

public enum TipoDiretorioEnum {
	PROTOCOLOS(1, "protocolos"),
	AIT(2, "ait");
	
	private final Integer key;
	private final String value;
	
	TipoDiretorioEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoDiretorioEnum.values().length; index++) {
			if(key == TipoDiretorioEnum.values()[index].getKey()) {
				return TipoDiretorioEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
