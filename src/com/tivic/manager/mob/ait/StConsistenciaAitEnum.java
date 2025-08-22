package com.tivic.manager.mob.ait;

public enum StConsistenciaAitEnum {
	ST_INCONSISTENTE(0, "Inconsistente"),
	ST_CONSISTENTE (1, "Consistente"),
	ST_PENDENTE_CONFIRMACAO (2, "Pendente de confirmação"),
	ST_EDUCATIVO (3, "Educativo");

	
	private final Integer key;
	private final String value;
	
	StConsistenciaAitEnum(Integer key, String value){
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
		for(Integer index=0; index < StConsistenciaAitEnum.values().length; index++) {
			if(key == StConsistenciaAitEnum.values()[index].getKey()) {
				return StConsistenciaAitEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
