package com.tivic.manager.grl.arquivo;

public enum TipoArquivoEnum {
	NAI_PUBLICADA(3, "Arquivo de NAIs publicadas"),
	NIP_PUBLICADA(5, "Arquivo de NIPs publicadas"),
	PROCESSOS_PUBLICADOS( 6, "Arquivo de PROCESSOS publicadas" );
	
	private final Integer key;
	private final String value;
	
	TipoArquivoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoArquivoEnum.values().length; index++) {
			if(key == TipoArquivoEnum.values()[index].getKey()) {
				return TipoArquivoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
