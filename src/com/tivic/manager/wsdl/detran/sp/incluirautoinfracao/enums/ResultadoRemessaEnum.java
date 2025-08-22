package com.tivic.manager.wsdl.detran.sp.incluirautoinfracao.enums;

public enum ResultadoRemessaEnum {

	REMESSA_GERADA_COM_ERRO(-1, "Falha ao gerar remessa."),
	REMESSA_GERADA_COM_SUCESSO(0, "Remessa gerada com sucesso."),
	REMESSA_GERADA_COM_INCONSISTENCIA(1, "Remessa gerada com inconsistência.");
	
	private final Integer key;
	private final String value;
	
	ResultadoRemessaEnum(Integer key, String value){
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
		for(Integer index=0; index < ResultadoRemessaEnum.values().length; index++) {
			if(key == ResultadoRemessaEnum.values()[index].getKey()) {
				return ResultadoRemessaEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}
