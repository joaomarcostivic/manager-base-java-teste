package com.tivic.manager.mob.lotes.enums.impressao;

public enum StatusLoteImpressaoEnum {
	AGUARDANDO_GERACAO(-1, "Aguardando geração"),
	ENVIO_IMPRESSAO(1, "Envio de impressão"),
	IMPRESSO(2, "IMPRESSO"),
	ENVELOPADO(3, "Em envelopamento"),
	EMBALADO(4, "Embalando"),
	ECARTAS_LOTE_CONSISTENTE(9, "Ecartas lote consistente"),
	ECARTAS_LOTE_INCONSISTENTE(10, "Ecartas lote inconsistente"),
	ECARTAS_LOTE_INVALIDO(11, "Ecartas lote invalido"),
	ECARTAS_LOTE_AUTORIZADO(12, "Ecartas lote autorizado"),
	ECARTAS_LOTE_REJEITADO(13, "Ecartas lote rejeitado"),
	AGUARDANDO_IMPRESSAO(14, "Aguardando impressão"),
    ECARTAS_SOLICITAR_CANCELAMENTO(19, "Ecartas solicitar cancelamento");
	
	final Integer key;
	private final String value;
	
	StatusLoteImpressaoEnum(Integer key, String value){
		this.key = key;
		this.value = value;
	}
	
	public Integer getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
    public static StatusLoteImpressaoEnum fromCode(int key) {
        for (StatusLoteImpressaoEnum status : StatusLoteImpressaoEnum.values()) {
            if (status.getKey() == key) {
                return status;
            }
        }
        throw new IllegalArgumentException("Erro ao obter valor do status do lote de impressão.");
    }
}
