package com.tivic.manager.ptc.portal.enums;

import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;

public enum TipoNmQualificacaoEnum {
	REQUERENTE(1, "REQUERENTE");
	
	private final Integer key;
	private final String value;
	
	TipoNmQualificacaoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoDocumentoProtocoloEnum.values().length; index++) {
			if(key == TipoDocumentoProtocoloEnum.values()[index].getKey()) {
				return TipoDocumentoProtocoloEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
