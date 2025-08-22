package com.tivic.manager.mob.aitpagamento.enums;

public enum TipoArrecadacaoEnum {

	GUICHE_DE_CAIXA(1, "Guichê de Caixa com fatura/guia de arrecadação."),
	ARRECADACAO_ELETRONICA(2, " Arrecadação Eletrônica com fatura/guia de arrecadação (terminais de auto - atendimento, ATM, home/office banking)."),
	INTERNET_COM_FATURA(3, "Internet com fatura/guia de arrecadação"),
	OUTROS_MEIOS_COM_FATURA(4, "Outros meios com fatura/guia de arrecadação."),
	CASAS_LOTERICAS(5, "Casas lotéricas/correspondentes bancários com fatura/guia de arrecadação"),
	TELEFONE_COM_FATURA(6, "Telefone com fatura/guia de arrecadação.");
	
	private final int key;
	private final String value;

	TipoArrecadacaoEnum(int key, String value) {
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
		for (int index = 0; index < TipoArrecadacaoEnum.values().length; index++) {
			if (key == TipoArrecadacaoEnum.values()[index].getKey()) {
				return TipoArrecadacaoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}