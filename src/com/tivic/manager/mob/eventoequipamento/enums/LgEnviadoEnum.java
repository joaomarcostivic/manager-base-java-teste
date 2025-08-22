package com.tivic.manager.mob.eventoequipamento.enums;

public enum LgEnviadoEnum {
	NAO_ENVIADO(0, "Evento não enviado"),
	ENVIADO(1, "Evento enviado");

	private final Integer key;
	private final String value;

	LgEnviadoEnum(Integer key, String value){
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
		for(Integer index=0; index < LgEnviadoEnum.values().length; index++) {
			if(key == LgEnviadoEnum.values()[index].getKey()) {
				return LgEnviadoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
