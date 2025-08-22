package com.tivic.manager.mob.orgaoaquivo.enums;

public enum TipoArquivoDocumento {
	CARTAO_PCD(1, "Cartão PCD");
	
	private final Integer key;
	private final String value;
	
	TipoArquivoDocumento(Integer key, String value){
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
		for(Integer index=0; index < TipoArquivoDocumento.values().length; index++) {
			if(key == TipoArquivoDocumento.values()[index].getKey()) {
				return TipoArquivoDocumento.values()[index].getValue();
			}
		}
		return null;
	}
}
