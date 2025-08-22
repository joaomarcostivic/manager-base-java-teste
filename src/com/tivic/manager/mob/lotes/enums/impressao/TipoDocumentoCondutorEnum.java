package com.tivic.manager.mob.lotes.enums.impressao;

import com.tivic.manager.mob.TipoDocumentoEnum;

public enum TipoDocumentoCondutorEnum {
	TP_DOCUMENTO_RENACH(1, "RENACH"),
	TP_DOCUMENTO_CPF(2, "CPF");
	
	private final Integer key;
	private final String value;
	
	TipoDocumentoCondutorEnum(Integer key, String value) {
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
		for(Integer index = 0; index < TipoDocumentoEnum.values().length; index++) {
			if(key == TipoDocumentoEnum.values()[index].getKey())
				return TipoDocumentoEnum.values()[index].getValue();
		}
		
		return null;
	}
}
