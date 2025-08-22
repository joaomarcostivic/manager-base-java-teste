package com.tivic.manager.ptc.protocolos.julgamento;

public enum TipoLoteJulgamentoEnum {
	
	LOTE_DEFESA_DEFERIDA( 8, "Lote de Defesas Deferidas" ),
	LOTE_JARI_COM_PROVIMENTO( 11, "Lote DE JARIs Deferidas" ),
	LOTE_JARI_SEM_PROVIMENTO( 12, "Lote DE JARIs Indeferidas" );

	
	private final Integer key;
	private final String value;
	
	TipoLoteJulgamentoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoLoteJulgamentoEnum.values().length; index++) {
			if(key == TipoLoteJulgamentoEnum.values()[index].getKey()) {
				return TipoLoteJulgamentoEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}
