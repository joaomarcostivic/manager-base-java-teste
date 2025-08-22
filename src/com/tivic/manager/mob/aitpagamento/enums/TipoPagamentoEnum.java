package com.tivic.manager.mob.aitpagamento.enums;

import com.tivic.manager.mob.AitPagamentoServices;

public enum TipoPagamentoEnum {

	PAGO_VIA_RENAINF(AitPagamentoServices.PAGO_VIA_RENAINF, "Pago Via RENAINF"),
	PAGO_VIA_BOLETO(AitPagamentoServices.PAGO_VIA_BOLETO, "Pago Via Boleto");

	private final int key;
	private final String value;

	TipoPagamentoEnum(int key, String value) {
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
		for (int index = 0; index < TipoPagamentoEnum.values().length; index++) {
			if (key == TipoPagamentoEnum.values()[index].getKey()) {
				return TipoPagamentoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}