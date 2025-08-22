package com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor;

public enum TipoDocumentoEnum {
	
	RENACH(2, "RENACH"),
	PGU(5, "PGU"),
	CPF(7, "CPF");
	
	private final Integer key;
	private final String value;
	
	TipoDocumentoEnum(Integer key, String value){
		this.key = key;
		this.value = value;
	}
	
	public final Integer getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public static String valueOf(Integer key) {
		for(Integer index=0; index < TipoDocumentoEnum.values().length; index++) {
			if(key == TipoDocumentoEnum.values()[index].getKey()) {
				return TipoDocumentoEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
