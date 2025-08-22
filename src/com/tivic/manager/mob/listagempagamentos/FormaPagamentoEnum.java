package com.tivic.manager.mob.listagempagamentos;

public enum FormaPagamentoEnum {
	DINHEIRO (1, "Dinheiro"),
	CHEQUE (2, "Cheque"),
	NAO_IDENTIFICADO (3, "Não identificado");
	
	private final int key;
	private final String value;
	
	FormaPagamentoEnum(int key, String value){
		this.key = key;
		this.value = value;
	}
	
	public int getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public static String valueOf(int key) {
		for(int index = 0; index < FormaPagamentoEnum.values().length; index++) {
			if(key == FormaPagamentoEnum.values()[index].getKey()) {
				return FormaPagamentoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
