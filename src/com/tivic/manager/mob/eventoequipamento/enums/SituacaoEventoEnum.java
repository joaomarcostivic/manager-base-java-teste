package com.tivic.manager.mob.eventoequipamento.enums;

import com.tivic.manager.mob.EventoEquipamentoServices;

public enum SituacaoEventoEnum {
	ST_EVENTO_NAO_PROCESSADO(EventoEquipamentoServices.ST_EVENTO_NAO_PROCESSADO, "EVENTO NÃO PROCESSADO"),
	ST_EVENTO_CONFIRMADO(EventoEquipamentoServices.ST_EVENTO_CONFIRMADO, "EVENTO CONFIRMADO"),
	ST_EVENTO_CANCELADO(EventoEquipamentoServices.ST_EVENTO_CANCELADO, "EVENTO CANCELADO"),
	ST_EVENTO_PENDENTE_CONFIRMACAO(EventoEquipamentoServices.ST_EVENTO_PENDENTE_CONFIRMACAO, "EVENTO PENDENTE DE CONFIRMAÇÃO"),
	ST_EVENTO_FINALIZADO(EventoEquipamentoServices.ST_EVENTO_FINALIZADO, "EVENTO FINALIZADO");

	private final Integer key;
	private final String value;

	SituacaoEventoEnum(Integer key, String value){
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
		for(Integer index=0; index < SituacaoEventoEnum.values().length; index++) {
			if(key == SituacaoEventoEnum.values()[index].getKey()) {
				return SituacaoEventoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}