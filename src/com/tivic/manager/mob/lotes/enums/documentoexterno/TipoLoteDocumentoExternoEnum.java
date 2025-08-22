package com.tivic.manager.mob.lotes.enums.documentoexterno;

public enum TipoLoteDocumentoExternoEnum {
	LOTE_OFICIO_PROTOCOLO_EXTERNO(17, "Lote de Ofício de Protocolo Externo");
	
	private final Integer key;
	private final String value;
	
	TipoLoteDocumentoExternoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoLoteDocumentoExternoEnum.values().length; index++) {
			if(key == TipoLoteDocumentoExternoEnum.values()[index].getKey()) {
				return TipoLoteDocumentoExternoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
