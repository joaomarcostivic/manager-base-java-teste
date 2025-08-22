package com.tivic.manager.mob.ait.sync.enuns;

public enum SituacaoExistEnum {
	
	NAO_EXISTE(0, "Não existe"),
	EXISTE(1, "Existe");
	
	private final Integer key;
	private final String value;
	
	SituacaoExistEnum(Integer key, String value) {
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
		for(Integer index = 0; index < SituacaoExistEnum.values().length; index++) {
			if(key == SituacaoExistEnum.values()[index].getKey())
				return SituacaoExistEnum.values()[index].getValue();
		}		
		return null;
	}
}
