package com.tivic.manager.wsdl.detran.mg.consultainfracoes;

public enum TipoConsultaEnum {
	
	CNH(1, "CNH"),
	CPF(2, "CPF");
	
	private final Integer key;
	private final String value;
	
	TipoConsultaEnum(Integer key, String value){
		this.key = key;
		this.value = value;
	}
	
	public final Integer getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public static String valueOf(Integer key) {
		for(Integer index=0; index < TipoConsultaEnum.values().length; index++) {
			if(key == TipoConsultaEnum.values()[index].getKey()) {
				return TipoConsultaEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
