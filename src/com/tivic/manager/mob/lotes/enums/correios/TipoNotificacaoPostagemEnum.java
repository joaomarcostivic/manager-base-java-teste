package com.tivic.manager.mob.lotes.enums.correios;

import com.tivic.manager.mob.AitMovimentoServices;

public enum TipoNotificacaoPostagemEnum {
	NAI(AitMovimentoServices.NAI_ENVIADO, "NAI"),
	NIP(AitMovimentoServices.NIP_ENVIADA, "NIP");
	
	private final Integer key;
	private final String value;
	
	TipoNotificacaoPostagemEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoNotificacaoPostagemEnum.values().length; index++) {
			if(key == TipoNotificacaoPostagemEnum.values()[index].getKey()) {
				return TipoNotificacaoPostagemEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
