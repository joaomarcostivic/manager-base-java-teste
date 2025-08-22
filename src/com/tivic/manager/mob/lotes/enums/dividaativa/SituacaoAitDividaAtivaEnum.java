package com.tivic.manager.mob.lotes.enums.dividaativa;

public enum SituacaoAitDividaAtivaEnum {
	
	INSERIDO(1, "Ait Inserido na Divida Ativa"),
	NAO_INSERIDO(2, "Ait não Inserido na Divida Ativa");

	private final Integer key;
	private final String value;
	
	SituacaoAitDividaAtivaEnum(Integer key, String value){
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
		for(Integer index=0; index < SituacaoAitDividaAtivaEnum.values().length; index++) {
			if(key == SituacaoAitDividaAtivaEnum.values()[index].getKey()) {
				return SituacaoAitDividaAtivaEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
