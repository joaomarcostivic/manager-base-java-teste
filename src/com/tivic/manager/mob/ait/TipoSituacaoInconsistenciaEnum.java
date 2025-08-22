package com.tivic.manager.mob.ait;

public enum TipoSituacaoInconsistenciaEnum {
	PENDETE(0, "Inconsistencia não resolvida"),
	RESOLVIDO( 1, "Inconsistencia resolvida" );
	
	private final Integer key;
	private final String value;
	
	TipoSituacaoInconsistenciaEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoSituacaoInconsistenciaEnum.values().length; index++) {
			if(key == TipoSituacaoInconsistenciaEnum.values()[index].getKey()) {
				return TipoSituacaoInconsistenciaEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
