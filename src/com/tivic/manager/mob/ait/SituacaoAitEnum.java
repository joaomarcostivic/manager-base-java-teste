package com.tivic.manager.mob.ait;

public enum SituacaoAitEnum {
	ST_CANCELADO(0, "Cancelado"),
	ST_CONFIRMADO (1, "Confirmado"),
	ST_PENDENTE_CONFIRMACAO (2, "Pendente de confirmação"),
	ST_EDUCATIVO (3, "Educativo");
	
	private final Integer key;
	private final String value;
	
	SituacaoAitEnum(Integer key, String value){
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
		for(Integer index=0; index < SituacaoAitEnum.values().length; index++) {
			if(key == SituacaoAitEnum.values()[index].getKey()) {
				return SituacaoAitEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
