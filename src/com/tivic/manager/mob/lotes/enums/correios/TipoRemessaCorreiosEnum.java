package com.tivic.manager.mob.lotes.enums.correios;

public enum TipoRemessaCorreiosEnum {
	REMESSA_ECONOMICA(1, "Remessa Econômica"),
	CARTA_REGISTRADA(2, "Carta Registrada"),
	CARTA_SIMPLES(3, "Carta Simples");
	
	private final Integer key;
	private final String value;
	
	TipoRemessaCorreiosEnum(Integer key, String value){
		this.key = key;
		this.value = value;
	}
		
	public Integer getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
    public static TipoRemessaCorreiosEnum fromCode(int key) {
        for (TipoRemessaCorreiosEnum status : TipoRemessaCorreiosEnum.values()) {
            if (status.getKey() == key) {
                return status;
            }
        }
        throw new IllegalArgumentException("Erro ao obter valor do tipo do lote de impressão.");
    }
}
