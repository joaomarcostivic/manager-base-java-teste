package com.tivic.manager.mob.lote.impressao;

public enum TipoRemessaCorreiosEnum {
	REMESSA_ECONOMICA(1, "Remessa Econômica"),
	CARTA_REGISTRADA(2, "Carta Registrada"),
	CARTA_SIMPLES(3, "Carta Simples");
	
	private final Integer key;
	private final String value;
	
	TipoRemessaCorreiosEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoRemessaCorreiosEnum.values().length; index++) {
			if(key == TipoRemessaCorreiosEnum.values()[index].getKey()) {
				return TipoRemessaCorreiosEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
