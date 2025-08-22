package com.tivic.manager.mob.infracao;

public enum TipoCompetenciaEnum {
	
	MUNICIPAL(0, "MUNICIPAL"),
	ESTADUAL(1, "ESTADUAL");
	
	private final Integer key;
	private final String value;
	
	TipoCompetenciaEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoCompetenciaEnum.values().length; index++) {
			if(key == TipoCompetenciaEnum.values()[index].getKey()) {
				return TipoCompetenciaEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}


