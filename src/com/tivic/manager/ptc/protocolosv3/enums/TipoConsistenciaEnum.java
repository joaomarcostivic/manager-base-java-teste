package com.tivic.manager.ptc.protocolosv3.enums;

public enum TipoConsistenciaEnum {
	CONSISTENTE(1, "CONSISTENTE"),
	INCONSISTENTE(2, "INCONSISTENTE");
	
	private final Integer key;
	private final String value;
	
	TipoConsistenciaEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoConsistenciaEnum.values().length; index++) {
			if(key == TipoConsistenciaEnum.values()[index].getKey()) {
				return TipoConsistenciaEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}
