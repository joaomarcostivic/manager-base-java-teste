package com.tivic.manager.mob.ecarta.enums;

public enum TipoArquivoRetornoEnum {
	RECIBO(0),
    INCONSISTENCIA(1);

    private final int valor;

    TipoArquivoRetornoEnum(int valor) {
        this.valor = valor;
    }

    public int getKey() {
        return valor;
    }
}
