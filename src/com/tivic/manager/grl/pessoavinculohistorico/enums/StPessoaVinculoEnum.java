package com.tivic.manager.grl.pessoavinculohistorico.enums;

public enum StPessoaVinculoEnum {
	ST_ATIVO(1, "Ativo"),
	ST_INATIVO(2, "Inativo");
	
	private final Integer key;
	private final String value;
	
	private StPessoaVinculoEnum(Integer key, String value) {
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
		for(Integer index=0; index < StPessoaVinculoEnum.values().length; index ++) {
			if(key == StPessoaVinculoEnum.values()[index].getKey()) {
				return StPessoaVinculoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
