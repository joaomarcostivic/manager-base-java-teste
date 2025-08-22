package com.tivic.manager.mob.infracao;

public enum TipoInfracaoEnum {

	MULTA_NAO_INDENTIFICACAO_CONDUTOR_FISICO(50002, "Multa de NIC Código Pessoa Fisica"),
	MULTA_NAO_INDENTIFICACAO_CONDUTOR_JURIDICO(50020, "Multa de NIC Código Pessoa Jurídica");
	
	private final Integer key;
	private final String value;
	
	TipoInfracaoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoInfracaoEnum.values().length; index++) {
			if(key == TipoInfracaoEnum.values()[index].getKey()) {
				return TipoInfracaoEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}
