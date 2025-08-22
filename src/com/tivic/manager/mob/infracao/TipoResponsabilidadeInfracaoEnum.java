package com.tivic.manager.mob.infracao;

public enum TipoResponsabilidadeInfracaoEnum {

	MULTA_RESPONSABILIDADE_CONDUTOR(0, "Multa de resposabilidade do condutor."),
	MULTA_RESPONSABILIDADE_PROPRIETARIO(1, "Multa de resposabilidade do proprietário");
	
	private final Integer key;
	private final String value;
	
	TipoResponsabilidadeInfracaoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoResponsabilidadeInfracaoEnum.values().length; index++) {
			if(key == TipoResponsabilidadeInfracaoEnum.values()[index].getKey()) {
				return TipoResponsabilidadeInfracaoEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}
