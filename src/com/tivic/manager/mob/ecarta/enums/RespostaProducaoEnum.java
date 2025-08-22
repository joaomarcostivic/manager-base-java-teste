package com.tivic.manager.mob.ecarta.enums;

public enum RespostaProducaoEnum {
    CONFIRMADO("A", "Produção do lote autorizada"),
    REJEITADO("B", "A produção do lote não está autorizada");

    private final String key;
    private final String value;

    RespostaProducaoEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}