package com.tivic.manager.mob;

public enum TipoSituacaoAitEnum {
	
	NAO_ENVIADO(AitMovimentoServices.NAO_ENVIADO, "Não Enviado"),
	REGISTRADO(AitMovimentoServices.REGISTRADO, "Registrado"),
	MOV_SEM_ENVIO(AitMovimentoServices.MOV_SEM_ENVIO, "Movimento sem Envio"),
	ENVIADO_AGUARDANDO(AitMovimentoServices.ENVIADO_AGUARDANDO, "Aguardando Confirmação"),
	NAO_ENVIAR(AitMovimentoServices.NAO_ENVIAR, "Não Enviar");
	
	private final Integer key;
	private final String value;
	
	TipoSituacaoAitEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoSituacaoAitEnum.values().length; index++) {
			if(key == TipoSituacaoAitEnum.values()[index].getKey()) {
				return TipoSituacaoAitEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
