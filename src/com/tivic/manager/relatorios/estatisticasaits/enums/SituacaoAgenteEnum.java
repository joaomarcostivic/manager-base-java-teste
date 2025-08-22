package com.tivic.manager.relatorios.estatisticasaits.enums;

public enum SituacaoAgenteEnum {
	ST_INATIVO(0, "Inativo"),
	ST_ATIVO(1, "Ativo");
	
	private final int key;
	private final String value;

	SituacaoAgenteEnum(int key, String value) {
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
		for (int index = 0; index < SituacaoAgenteEnum.values().length; index++) {
			if (key == SituacaoAgenteEnum.values()[index].getKey()) {
				return SituacaoAgenteEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
