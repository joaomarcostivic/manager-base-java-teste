package com.tivic.manager.triagem.enums;

public enum StEventoEstacionamentoDigitalEnum {
	
	PENDETE(0, "Pendente de processamento"),
	PROCESSADO(1, "Processado");
	
	private final Integer key;
	private final String value;
	
	StEventoEstacionamentoDigitalEnum(Integer key, String value){
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
		for(Integer index=0; index < StEventoEstacionamentoDigitalEnum.values().length; index++) {
			if(key == StEventoEstacionamentoDigitalEnum.values()[index].getKey()) {
				return StEventoEstacionamentoDigitalEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
