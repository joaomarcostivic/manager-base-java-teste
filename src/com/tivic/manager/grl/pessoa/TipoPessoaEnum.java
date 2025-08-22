package com.tivic.manager.grl.pessoa;

public enum TipoPessoaEnum {
	FISICA(0, "Física"),
	JURIDICA(1, "Jurídica");
	
	private final Integer key;
	private final String value;
	
	TipoPessoaEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoPessoaEnum.values().length; index++) {
			if(key == TipoPessoaEnum.values()[index].getKey()) {
				return TipoPessoaEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
