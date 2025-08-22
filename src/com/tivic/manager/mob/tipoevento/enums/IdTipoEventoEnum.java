package com.tivic.manager.mob.tipoevento.enums;

public enum IdTipoEventoEnum {
	
	EST("EST", "Estatística"),
	ASV("ASV", "Avanço de Sinal Vermelho"),
	PSF("PSF", "Parada Sobre a Faixa"),
	CEX("CEX", "Circulação Exclusiva"),
	LNP("LNP", "Local Não Permitido"),
	LHP("LHP", "Transitar em Local e Horário não Permitidos"),
	RLP("RLP", "Retorno em Local Proibido"),
	CLP("CLP", "Conversão em Local Proibido"),
	VAL("VAL", "Velocidade Além do Limite"),
	NED("NED", "Notificação de Estacionamento Digital"),
	IMT("IMT", "IMT");
	
	private final String key;
	private final String value;
	
	IdTipoEventoEnum(String key, String value){
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
}

