package com.tivic.manager.ptc.protocolosv3.enums;

public enum TipoDocumentoProtocoloEnum {
	APRESENTACAO_CONDUTOR(16, "Apresentação de Condutor"),
	DEFESA_PREVIA(17, "Defesa Prévia"),
	RECURSO_JARI(18, "Recurso Jari"),
	RESTITUIÇÃO(19, "Restituição"),
	RECURSO_CETRAN(20, "Recurso CETRAN"),
	ATA(21, "Ata"),
	DEFESA_PREVIA_ADVERTENCIA(22, "Defesa Prévia (Advertência)"),
	CARTAO_IDOSO(100, "Cartão do idoso"),
	CARTAO_PCD(101, "Cartão de PCD");
	
	private final Integer key;
	private final String value;
	
	TipoDocumentoProtocoloEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoDocumentoProtocoloEnum.values().length; index++) {
			if(key == TipoDocumentoProtocoloEnum.values()[index].getKey()) {
				return TipoDocumentoProtocoloEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
