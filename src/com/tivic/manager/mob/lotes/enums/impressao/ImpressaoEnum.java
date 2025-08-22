package com.tivic.manager.mob.lotes.enums.impressao;

public enum ImpressaoEnum {
	NAO_IMPRIMIR(0, "Não imprimir"),
	IMPRIMIR(1, "Imprimir");
	
	private final Integer key;
	private final String value;
	
	ImpressaoEnum(Integer key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public Integer getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
}
