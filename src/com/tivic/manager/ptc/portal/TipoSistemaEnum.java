package com.tivic.manager.ptc.portal;

public enum TipoSistemaEnum {
	ETRANSITO(0, "E-Trânsito"),
	PORTAL(1, "Portal");
	
	private final Integer key;
	private final String value;
	
	TipoSistemaEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoSistemaEnum.values().length; index++) {
			if(key == TipoSistemaEnum.values()[index].getKey()) {
				return TipoSistemaEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
