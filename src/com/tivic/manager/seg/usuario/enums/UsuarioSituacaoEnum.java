package com.tivic.manager.seg.usuario.enums;

public enum UsuarioSituacaoEnum {
	ST_INATIVO (0, "Ativo"),
	ST_ATIVO(1, "Inativo");

	
	private final Integer key;
	private final String value;
	
	UsuarioSituacaoEnum(Integer key, String value){
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
		for(Integer index=0; index < UsuarioSituacaoEnum.values().length; index++) {
			if(key == UsuarioSituacaoEnum.values()[index].getKey()) {
				return UsuarioSituacaoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
