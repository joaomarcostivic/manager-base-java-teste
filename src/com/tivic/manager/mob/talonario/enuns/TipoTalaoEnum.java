package com.tivic.manager.mob.talonario.enuns;


public enum TipoTalaoEnum {
	
	TP_TALONARIO_TRANSITO(0,"Talonário de AIT"),
	TP_TALONARIO_ELETRONICO_TRANSITO(1, "Talonário Eletrônico de AIT"),
	TP_TALONARIO_TRANSPORTE(2, "Talonário do Transporte"),
	TP_TALONARIO_ELETRONICO_TRANSPORTE(3, "Talonário Eletrônico do Transporte"),
	TP_TALONARIO_NIC(4, "Talonário Eletrônico de AIT"),
	TP_TALONARIO_BOAT(5, "Talonário BOAT"),
	TP_TALONARIO_ELETRONICO_BOAT(6, "Talonário Eletrônico de BOAT"),
	TP_TALONARIO_RRD(7, "Talonário de RRD"),
	TP_TALONARIO_ELETRONICO_RRD(8, "Talonário Eletrônico de RRD"),
	TP_TALONARIO_TRRAV(9, "Talonário de TRRAV"),
	TP_TALONARIO_ELETRONICO_TRRAV(10, "Talonário Eletrônico de TRRAV"),
	TP_TALONARIO_TRANSPORTE_NIC(11, "Talonário de NIC"),
	TP_TALONARIO_VIDEO_MONITORAMENTO(12, "Talonário de AIT (Vídeo Monitoramento)"),
	TP_TALONARIO_RADAR_ESTATICO(13, "Talonário de AIT (Radar Estático)"),
	TP_TALONARIO_RADAR_FIXO(14, "Talonário de AIT (Radar Fixo)"),
	TP_TALONARIO_ZONA_AZUL(15, "Trânsito - Área Azul");
	
	private final Integer key;
	private final String value;
	
	TipoTalaoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoTalaoEnum.values().length; index++) {
			if(key == TipoTalaoEnum.values()[index].getKey()) {
				return TipoTalaoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
