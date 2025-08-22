package com.tivic.manager.mob.aitmovimento.aitmovimentobuilder;

public enum TipoSituacaoPublicacaoDO {
	NAO_PUBLICADO_DO(0, "Não publicado"),
	PUBLICADO_DO(1, "Publicado");
	
	private final Integer key;
	private final String value;
	
	TipoSituacaoPublicacaoDO(Integer key, String value){
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
		for(Integer index=0; index < TipoSituacaoPublicacaoDO.values().length; index++) {
			if(key == TipoSituacaoPublicacaoDO.values()[index].getKey()) {
				return TipoSituacaoPublicacaoDO.values()[index].getValue();
			}
		}
		return null;
	}
}
