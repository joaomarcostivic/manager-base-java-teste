package com.tivic.manager.mob.aitmovimento;

import com.tivic.manager.mob.AitMovimentoServices;

public enum TipoLgEnviadoDetranEnum {
	REGISTRO_CANCELADO(AitMovimentoServices.REGISTRO_CANCELADO, "Registro Cancelado"),
	NAO_ENVIADO(AitMovimentoServices.NAO_ENVIADO, "Registro Não Enviado"),
	REGISTRADO(AitMovimentoServices.REGISTRADO, "Registro Enviado"),
	MOV_SEM_ENVIO(AitMovimentoServices.MOV_SEM_ENVIO, "Movimento Sem Envio"),
	ENVIADO_AGUARDANDO(AitMovimentoServices.ENVIADO_AGUARDANDO, "Registro Enviado e Aguardando"),
	NAO_ENVIAR_CANCELADO(AitMovimentoServices.NAO_ENVIAR, "Não Enviar/Cancelado");
	
	private final Integer key;
	private final String value;
	
	TipoLgEnviadoDetranEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoLgEnviadoDetranEnum.values().length; index++) {
			if(key == TipoLgEnviadoDetranEnum.values()[index].getKey()) {
				return TipoLgEnviadoDetranEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
