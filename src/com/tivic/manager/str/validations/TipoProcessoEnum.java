package com.tivic.manager.str.validations;

import com.tivic.manager.str.ProcessoServices;

public enum TipoProcessoEnum {
	NAO_INFORMADO(-1, "Não Informado"),
	TP_DEFESA_PREVIA(ProcessoServices.TP_DEFESA_PREVIA, "Defesa Prévia"),
	TP_JARI(ProcessoServices.TP_JARI, "JARI"),
	TP_APRESENTACAO_CONDUTOR(ProcessoServices.TP_APRESENTAR_CONDUTOR, "Apresentação Condutor"),
	TP_REEMBOLSO(ProcessoServices.TP_REEMBOLSO, "Reembolso"),
	TP_CETRAN(ProcessoServices.TP_CETRAN, "CETRAN");
	
	private final Integer key;
	private final String value;
	
	TipoProcessoEnum(Integer key, String value) {
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
		for(Integer index = 0; index < TipoProcessoEnum.values().length; index++) {
			if(key == TipoProcessoEnum.values()[index].getKey())
				return TipoProcessoEnum.values()[index].getValue();
		}
		
		return null;
	}
}
