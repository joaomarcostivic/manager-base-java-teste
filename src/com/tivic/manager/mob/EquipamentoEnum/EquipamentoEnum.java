package com.tivic.manager.mob.EquipamentoEnum;

import com.tivic.manager.mob.EquipamentoServices;

public enum EquipamentoEnum {
	
	TALONARIO_ELETRONICO(EquipamentoServices.TALONARIO_ELETRONICO, "TALONÁRIO ELETRÔNICO"),
	SEMAFORO(EquipamentoServices.SEMAFORO, "SEMÁFORO"),
	RADAR_FIXO(EquipamentoServices.RADAR_FIXO, "RADAR FIXO"),
	RADAR_MOVEL(EquipamentoServices.RADAR_MOVEL, "RADAR MÓVEL"),
	GPS(EquipamentoServices.GPS, "GPS"),
	TAXIMETRO(EquipamentoServices.TAXIMETRO, "TAXÍMETRO"),
	IMPRESSORA(EquipamentoServices.IMPRESSORA, "IMPRESSORA"),
	FISCALIZADOR(EquipamentoServices.FISCALIZADOR, "FISCALIZADOR"),
	TACOGRAFO(EquipamentoServices.TACOGRAFO, "TACÓGRAFO"),
	CAMERA(EquipamentoServices.CAMERA, "CÂMERA"),
	RADAR_ESTATICO(EquipamentoServices.RADAR_ESTATICO, "RADAR ESTÁTICO"),
	DETECTOR(EquipamentoServices.DETECTOR, "RADAR DETECTOR"),
	ZONA_AZUL(EquipamentoServices.ZONA_AZUL, "ZONA AZUL"),
	NENHUM(-1, "NENHUM EQUIPAMENTO (MANUAL)");
	
	private final Integer key;
	private final String value;
	
	EquipamentoEnum(Integer key, String value){
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
		for(Integer index=0; index < EquipamentoEnum.values().length; index++) {
			if(key == EquipamentoEnum.values()[index].getKey()) {
				return EquipamentoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
