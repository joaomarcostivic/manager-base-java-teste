package com.tivic.manager.mob.lotes.enums.impressao;

public enum TipoLoteImpressaoEnum {	
	LOTE_NAI_VIA_UNICA(0, "Lote de NAI (Via Única)"),
	LOTE_NIP_VIA_UNICA(1, "Lote de NIP (Via Única)"),
	LOTE_FIM_PRAZO_DEFESA_VIA_UNICA(2, "Lote de Movimentos Para O Fim Prazo Defesa"),
	LOTE_NAI(3, "Lote de NAIs"),
	LOTE_NIP(5, "Lote de NIPs"),
	NOVO_PRAZO_DEFESA(8, "NOVO PRAZO PARA ABERTURA DE DEFESA DA AUTUACAO"),
	LOTE_NIC_NAI_VIA_UNICA(10, "NIC - Lote de NAI (Via Única)"),
	LOTE_NIC_NIP_VIA_UNICA(11, "NIC - Lote de NIP (Via Única)"),
	LOTE_NIC_NAI( 12, "Lote de NIC-NAIs" ),
	LOTE_NIC_NIP( 13, "Lote de NIC-NIPs" ),
	LOTE_FIM_PRAZO_DEFESA(39, "Lote de Movimentos Para O Fim Prazo Defesa");
	
	private final Integer key;
	private final String value;
	
	TipoLoteImpressaoEnum(Integer key, String value){
		this.key = key;
		this.value = value;
	}
	
	public Integer getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
    public static TipoLoteImpressaoEnum fromCode(Integer key) {
        for (TipoLoteImpressaoEnum status : TipoLoteImpressaoEnum.values()) {
            if (status.getKey() == key) {
                return status;
            }
        }
        throw new IllegalArgumentException("Erro ao obter valor do tipo do lote de impressão.");
    }
}
