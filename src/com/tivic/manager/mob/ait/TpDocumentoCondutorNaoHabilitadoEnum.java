package com.tivic.manager.mob.ait;

public enum TpDocumentoCondutorNaoHabilitadoEnum {

	CPF(1, "CPF"),
	CNPJ(2, "CNPJ"),
	RG(3, "REGISTRO GERAL"),
	RNE(4,"REGISTRO NACIONAL DE ESTRANGEIROS"),
	OUTROS(5, "OUTROS"),
	NAO_APRESENTADO(9, "NÃO APRESENTOU DOCUMENTO");
	
	private final Integer key;
	private final String value;
	
	
	TpDocumentoCondutorNaoHabilitadoEnum(Integer key, String value){
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
		for(Integer index = 0; index < TpDocumentoCondutorNaoHabilitadoEnum.values().length; index++) {
			if(key == TpDocumentoCondutorNaoHabilitadoEnum.values()[index].getKey())
				return TpDocumentoCondutorNaoHabilitadoEnum.values()[index].getValue();
		}
		
		return null;
	}
}
