package com.tivic.manager.mob.ocorrencia;

public enum TipoOcorrenciaEnum {
	OCORRENCIA_APP(5, "Ocorrencia para aplicativo");
	
	private final Integer key;
	private final String value;
	
	TipoOcorrenciaEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoOcorrenciaEnum.values().length; index++) {
			if(key == TipoOcorrenciaEnum.values()[index].getKey()) {
				return TipoOcorrenciaEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
