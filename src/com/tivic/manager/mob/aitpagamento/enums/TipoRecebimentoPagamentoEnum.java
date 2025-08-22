package com.tivic.manager.mob.aitpagamento.enums;

public enum TipoRecebimentoPagamentoEnum {

	PAGAMENTO_FINALIZADO(0, "Pagamento Finalizado."),
	PAGAMENTO_ESTORNADO(2, "Pagamento Estornado.");
	
	private final int key;
	private final String value;

	TipoRecebimentoPagamentoEnum(int key, String value) {
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
		for (int index = 0; index < TipoRecebimentoPagamentoEnum.values().length; index++) {
			if (key == TipoRecebimentoPagamentoEnum.values()[index].getKey()) {
				return TipoRecebimentoPagamentoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}