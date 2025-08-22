package com.tivic.manager.mob;

public enum TipoLoteCorreiosEnum {
	E_CARTAS( CorreiosLoteServices.E_CARTAS, "E-Cartas" ),
	REMESSA_ECONOMICA( CorreiosLoteServices.REMESSA_ECONOMICA, "Remessa Econômica" ),
	CARTA_REGISTRADA( CorreiosLoteServices.CARTA_REGISTRADA, "Carta Registrada" );
	
	private final Integer key;
	private final String value;
	
	TipoLoteCorreiosEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoLoteCorreiosEnum.values().length; index++) {
			if(key == TipoLoteCorreiosEnum.values()[index].getKey()) {
				return TipoLoteCorreiosEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
