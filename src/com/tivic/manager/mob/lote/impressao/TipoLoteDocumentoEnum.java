package com.tivic.manager.mob.lote.impressao;

public enum TipoLoteDocumentoEnum {

	LOTE_NAI_VIA_UNICA(0, "Lote de NAI (Via Única)"),
	LOTE_NIP_VIA_UNICA(1, "Lote de NIP (Via Única)"),
	LOTE_FIM_PRAZO_DEFESA_VIA_UNICA(2, "Lote de Movimentos Para O Fim Prazo Defesa"),
	LOTE_NAI( 3, "Lote de NAIs" ),
	LOTE_NIP( 5, "Lote de NIPs" ),
	LOTE_PUBLICACAO_RESULTADO_JARI(6, "Lote de Publicação de JARI no D.O"),
	LOTE_PUBLICACAO_RESULTADO_DEFESA(7, "Lote de Publicação de DEFESA no D.O"),
	LOTE_CARTA_JULGAMETNO_DEFESA_DEFERIDA( 8, "Lote de Carta de Julgamento para Defesas Deferidas" ),
	LOTE_PUBLICACAO_NAI( 9, "EDITAL DA NOTIFICAÇÃO DE AUTUAÇÃO DE INFRAÇÕES DE TRÂNSITO." ),
	LOTE_PUBLICACAO_NIP( 10, "EDITAL DA NOTIFICAÇÃO DE IMPOSIÇÃO DE PENALIDADE DE TRÂNSITO." ),
	LOTE_CARTA_JULGAMETNO_JARI_COM_PROVIMENTO( 11, "Lote de Carta de Julgamento para JARIs Deferidas" ),
	LOTE_CARTA_JULGAMETNO_JARI_SEM_PROVIMENTO( 12, "Lote de Carta de Julgamento para JARIs Indeferidas" ),
	LOTE_DIVIDA_ATIVA(13, "Lote de Divida Ativa"),
	LOTE_OFICIO_PROTOCOLO_EXTERNO(17, "Lote de Ofício de Protocolo Externo"),
	LOTE_FIM_PRAZO_DEFESA(39, "Lote de Movimentos Para O Fim Prazo Defesa"),
	LOTE_PUBLICACAO_RESULTADO_DEFESA_ADVERTENCIA(104, "Lote de Publicação de DEFESA com Advertência no D.O");

	private final Integer key;
	private final String value;
	
	TipoLoteDocumentoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoLoteDocumentoEnum.values().length; index++) {
			if(key == TipoLoteDocumentoEnum.values()[index].getKey()) {
				return TipoLoteDocumentoEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}
