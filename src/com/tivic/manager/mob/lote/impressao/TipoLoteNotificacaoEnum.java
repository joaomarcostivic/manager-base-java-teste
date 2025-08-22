package com.tivic.manager.mob.lote.impressao;

public enum TipoLoteNotificacaoEnum {
	LOTE_NAI_VIA_UNICA(0, "Lote de NAI (Via Única)"),
	LOTE_NIP_VIA_UNICA(1, "Lote de NIP (Via Única)"),
	LOTE_NIC_NAI_VIA_UNICA(10, "NIC - Lote de NAI (Via Única)"),
	LOTE_NIC_NIP_VIA_UNICA(11, "NIC - Lote de NIP (Via Única)"),
	LOTE_FIM_PRAZO_DEFESA_VIA_UNICA(2, "Lode de Movimentos Para O Fim Prazo Defesa"),
	LOTE_NAI( 3, "Lote de NAIs" ),
	LOTE_NIP( 5, "Lote de NIPs" ),
	LOTE_NIC_NAI( 12, "NIC - Lote de NAIs" ),
	LOTE_NIC_NIP( 13, "NIC - Lote de NIPs" ),
	LOTE_FIM_PRAZO_DEFESA(39, "Lode de Movimentos Para O Fim Prazo Defesa");
	
	final Integer key;
	private final String value;
	
	TipoLoteNotificacaoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoLoteNotificacaoEnum.values().length; index++) {
			if(key == TipoLoteNotificacaoEnum.values()[index].getKey()) {
				return TipoLoteNotificacaoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
