package com.tivic.manager.mob.correios;

public enum TipoEtiquetaEnum {
	E_CARTAS(0, "eCartas"),
	REMESSA_ECONOMICA(1, "Remessa Econômica"),
	CARTA_REGISTRADA(2, "Carta Registrada");
	
	private final Integer key;
	private final String value;
	
	TipoEtiquetaEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoEtiquetaEnum.values().length; index++) {
			if(key == TipoEtiquetaEnum.values()[index].getKey()) {
				return TipoEtiquetaEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
