package com.tivic.manager.triagem.enums;

public enum SituacaoEventoEquipamentoEnum {
    NAO_PROCESSADO(0, "Não processado"),
    CONFIRMADO(1, "Confirmado"),
    CANCELADO(2, "Cancelado");
	
	private final Integer key;
	private final String value;
	
	SituacaoEventoEquipamentoEnum(Integer key, String value){
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
		for(Integer index=0; index < SituacaoEventoEquipamentoEnum.values().length; index++) {
			if(key == SituacaoEventoEquipamentoEnum.values()[index].getKey()) {
				return SituacaoEventoEquipamentoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
