package com.tivic.manager.mob.lotes.enums.publicacao;

public enum TipoLotePublicacaoEnum {
	LOTE_PUBLICACAO_RESULTADO_JARI(6, "Lote de Publicação de JARI no D.O"),
	LOTE_PUBLICACAO_RESULTADO_DEFESA(7, "Lote de Publicação de DEFESA no D.O"),
	LOTE_PUBLICACAO_NAI( 9, "Lote de Publicação de NA no D.O" ),
	LOTE_PUBLICACAO_NIP( 10, "Lote de Publicação de NP no D.O" ),
	LOTE_PUBLICACAO_RESULTADO_DEFESA_ADVERTENCIA(104, "Lote de Publicação de DEFESA com Advertência no D.O");

	private final Integer key;
	private final String value;
	
	TipoLotePublicacaoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoLotePublicacaoEnum.values().length; index++) {
			if(key == TipoLotePublicacaoEnum.values()[index].getKey()) {
				return TipoLotePublicacaoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
