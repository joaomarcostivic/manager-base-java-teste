package com.tivic.manager.mob.aitpagamento.enums;

public enum SituacaoPagamentoEnum {
	CANCELADO(0, "Cancelado"),
	EM_ABERTO (1, "Em aberto"),
	RECEBIDO (2, "Recebido"),
	RENEGOCIADO(3, "Renegociado");
	
	private final int key;
	private final String value;
	
	SituacaoPagamentoEnum(int key, String value){
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
		for(int index = 0; index < SituacaoPagamentoEnum.values().length; index++) {
			if(key == SituacaoPagamentoEnum.values()[index].getKey()) {
				return SituacaoPagamentoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
