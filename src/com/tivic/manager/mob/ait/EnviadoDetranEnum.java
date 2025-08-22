package com.tivic.manager.mob.ait;

import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;

public enum EnviadoDetranEnum {
	LG_DETRAN_NAO_ENVIADA(0, "Não enviada"),
	LG_DETRAN_ENVIADA (1, "Enviada");
	
	private final Integer key;
	private final String value;
	
	EnviadoDetranEnum(Integer key, String value){
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
		for(Integer index=0; index < EquipamentoEnum.values().length; index++) {
			if(key == EquipamentoEnum.values()[index].getKey()) {
				return EquipamentoEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
