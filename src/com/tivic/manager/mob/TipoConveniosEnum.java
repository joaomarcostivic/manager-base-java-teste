package com.tivic.manager.mob;

public enum TipoConveniosEnum {
	
	DETRAN( AitServices.TP_CONVENIO_DETRAN, "DETRAN" ),
	TP_CONVENIO_PRE( AitServices.TP_CONVENIO_PRE, "PRE - SIT (estadual)" ),
	TP_CONVENIO_PRF( AitServices.TP_CONVENIO_PRF, "PRF - DNIT (federal)" ),
	TP_CONVENIO_GM( AitServices.TP_CONVENIO_GM, "GM - Guarda Municipal" ),
	TP_CONVENIO_PM( AitServices.TP_CONVENIO_PM, "PM - Polícia Militar (estadual)" ),
	TP_CONVENIO_PC( AitServices.TP_CONVENIO_PC, "PC - Polícia Civil" );
	
	private final Integer key;
	private final String value;
	
	TipoConveniosEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoConveniosEnum.values().length; index++) {
			if(key == TipoConveniosEnum.values()[index].getKey()) {
				return TipoConveniosEnum.values()[index].getValue();
			}
		}
		return null;
	}
}