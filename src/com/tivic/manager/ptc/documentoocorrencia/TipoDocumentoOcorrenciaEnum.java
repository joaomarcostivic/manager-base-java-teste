package com.tivic.manager.ptc.documentoocorrencia;

public enum TipoDocumentoOcorrenciaEnum {
	ANULACAO_DE_CI(1, "Lote de Defesas Deferidas"), 
	MUDANCA_DE_SITUACAO(2, "Lote DE JARIs Deferidas"),
	IMPRESSAO(3, "Lote DE JARIs Indeferidas"), 
	PUBLICACAO(4, "Lote DE JARIs Indeferidas"),
	DEFERIDA(5, "Lote DE JARIs Indeferidas"), 
	INDEFERIDA(6, "Lote DE JARIs Indeferidas");

	private final Integer key;
	private final String value;

	TipoDocumentoOcorrenciaEnum(Integer key, String value) {
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
		for (Integer index = 0; index < TipoDocumentoOcorrenciaEnum.values().length; index++) {
			if (key == TipoDocumentoOcorrenciaEnum.values()[index].getKey()) {
				return TipoDocumentoOcorrenciaEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
