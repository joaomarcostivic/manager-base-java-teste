package com.tivic.manager.mob;

public enum DeclaranteTipoRelacaoEnum {

	NAO_INFORMADO(-1, "Não Informado"),
	TP_CONDUTOR(BoatDeclaranteServices.TP_CONDUTOR, "Condutor"),
	TP_CONDUTOR_E_PROPRIETARIO(BoatDeclaranteServices.TP_CONDUTOR_E_PROPRIETARIO, "Condutor e Proprietário"),
	TP_PROPRIETARIO(BoatDeclaranteServices.TP_PROPRIETARIO, "Proprietário"),
	TP_TERCEIRO_ENVOLVIDO(BoatDeclaranteServices.TP_TERCEIRO_ENVOLVIDO, "Terceiro Envolvido");
	
	private final Integer key;
	private final String value;
	
	DeclaranteTipoRelacaoEnum(Integer key, String value) {
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
		for(Integer index = 0; index < DeclaranteTipoRelacaoEnum.values().length; index++) {
			if(key == DeclaranteTipoRelacaoEnum.values()[index].getKey())
				return DeclaranteTipoRelacaoEnum.values()[index].getValue();
		}
		
		return null;
	}
	
}