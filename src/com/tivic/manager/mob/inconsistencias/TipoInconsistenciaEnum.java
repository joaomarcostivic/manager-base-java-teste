package com.tivic.manager.mob.inconsistencias;

public enum TipoInconsistenciaEnum {
	
	TP_INDEFERIMENTO( 1, "Inconsistência indeferida" ),
	TP_CORRETIVO( 2, "Inconsistência disponível para correção" );
	
	private final Integer key;
	private final String value;
	
	TipoInconsistenciaEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoInconsistenciaEnum.values().length; index++) {
			if(key == TipoInconsistenciaEnum.values()[index].getKey()) {
				return TipoInconsistenciaEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}
