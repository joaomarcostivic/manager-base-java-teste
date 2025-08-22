package com.tivic.manager.mob.ecarta.enums;

public enum CodigoCorreiosCepInvalidoEnum {
	CODIGO_ERRO_23("23"),
	CODIGO_ERRO_24("24");

    private final String valor;

    CodigoCorreiosCepInvalidoEnum(String valor) {
        this.valor = valor;
    }

    public String getKey() {
        return valor;
    }
}
