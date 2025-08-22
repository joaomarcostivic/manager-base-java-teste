package com.tivic.manager.mob.boat.enums;

public enum TipoAcidenteBoatEnum {
	
	COLISAO_LATERAL(0, "Colisão Lateral"),
	COLISAO_FRONTAL(1, "Colisão Frontal"),
	COLISAO_TRANSVERSAL(2, "Colisão Transversal"),
	COLISAO_TRASEIRA(3, "Colisão Traseira"),
	DERRAMAMENTO_DE_CARGA(4, "Derramamento de Carga"),
	ATROPELAMENTO_ANIMAL(5, "Atropelamento de Animal"),
	SAIDA_DE_PISTA(6, "Saída de Pista"),
	COLISAO_COM_OBJETO_MOVEL(7, "Colisão com Objeto Móvel"),
	CHOQUE(8, "Choque"),
	ENGAVETAMENTO(9, "Engavetamento"),
	TOMBAMENTO(10, "Tombamento"),
	OUTRO(11, "Outro");
	
	private final Integer key;
	private final String value;
	
	TipoAcidenteBoatEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoAcidenteBoatEnum.values().length; index++) {
			if(key == TipoAcidenteBoatEnum.values()[index].getKey()) {
				return TipoAcidenteBoatEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
