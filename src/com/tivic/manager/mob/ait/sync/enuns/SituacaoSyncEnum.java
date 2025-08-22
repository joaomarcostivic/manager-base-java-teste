package com.tivic.manager.mob.ait.sync.enuns;

public enum SituacaoSyncEnum {
	
	NAO_SINCRONIZADO(0, "Não sincronizado"),
	SINCRONIZADO(1, "Sincronizado");
	
	private final Integer key;
	private final String value;
	
	SituacaoSyncEnum(Integer key, String value) {
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
		for(Integer index = 0; index < SituacaoSyncEnum.values().length; index++) {
			if(key == SituacaoSyncEnum.values()[index].getKey())
				return SituacaoSyncEnum.values()[index].getValue();
		}		
		return null;
	}
}
