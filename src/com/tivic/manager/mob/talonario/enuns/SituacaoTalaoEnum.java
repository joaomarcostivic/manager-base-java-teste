package com.tivic.manager.mob.talonario.enuns;

public enum SituacaoTalaoEnum {
	
	ST_TALAO_INATIVO(0, "Talão Inativo"),
	ST_TALAO_ATIVO(1, "Talão Ativo"),
	ST_TALAO_CONCLUIDO(2, "Talão Concluido"),
	ST_TALAO_CONFERIDO(3, "Talão Conferido"),
	ST_TALAO_DIVERGENTE(4, "Talão Divergente"),
	ST_TALAO_PENDENTE(5, "Talão Pendente");
	
	private final Integer key;
	private final String value;
	
	SituacaoTalaoEnum(Integer key, String value) {
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
		for(Integer index = 0; index < SituacaoTalaoEnum.values().length; index++) {
			if(key == SituacaoTalaoEnum.values()[index].getKey())
				return SituacaoTalaoEnum.values()[index].getValue();
		}		
		return null;
	}
}
