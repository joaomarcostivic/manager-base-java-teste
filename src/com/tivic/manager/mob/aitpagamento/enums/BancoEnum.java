package com.tivic.manager.mob.aitpagamento.enums;

public enum BancoEnum {	
    BANCO_BRASIL(001, "Banco do Brasil"),
    BRADESCO(237, "Bradesco"),
    CAIXA(104, "Caixa Econômica"),
    ITAU(341, "Itau"); 

	private final int key;
	private final String value;

	BancoEnum(int key, String value) {
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
		for (int index = 0; index < BancoEnum.values().length; index++) {
			if (key == BancoEnum.values()[index].getKey()) {
				return BancoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
