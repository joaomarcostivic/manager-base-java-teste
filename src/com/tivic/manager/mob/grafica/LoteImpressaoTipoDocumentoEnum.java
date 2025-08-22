package com.tivic.manager.mob.grafica;

public enum LoteImpressaoTipoDocumentoEnum {
	
	NAI(0, "Lote NAI"),
	NIP(1, "Lote NIP");
	
	private final Integer key;
	private final String value;
	
	LoteImpressaoTipoDocumentoEnum(Integer key, String value){
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
		for(Integer index=0; index < LoteImpressaoTipoDocumentoEnum.values().length; index++) {
			if(key == LoteImpressaoTipoDocumentoEnum.values()[index].getKey()) {
				return LoteImpressaoTipoDocumentoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
