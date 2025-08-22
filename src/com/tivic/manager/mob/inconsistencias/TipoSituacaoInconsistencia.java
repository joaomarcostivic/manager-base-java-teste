package com.tivic.manager.mob.inconsistencias;

public enum TipoSituacaoInconsistencia {

	PENDENTE( 0, "Pendente" ),
	RESOLVIDO( 1, "Resolvida" ),
	IGNORADO( 2, "Ignorada" ),
	CANCELADO(3, "Cancelado");
	
	private final Integer key;
	private final String value;
	
	TipoSituacaoInconsistencia(Integer key, String value){
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
		for(Integer index=0; index < TipoSituacaoInconsistencia.values().length; index++) {
			if(key == TipoSituacaoInconsistencia.values()[index].getKey()) {
				return TipoSituacaoInconsistencia.values()[index].getValue();
			}
		}
		return null;
	}
}
