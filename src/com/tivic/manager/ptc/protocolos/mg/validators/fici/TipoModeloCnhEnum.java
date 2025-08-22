package com.tivic.manager.ptc.protocolos.mg.validators.fici;

public enum TipoModeloCnhEnum {
	PGU(0, "PGU"),
	RENACH(1, "RENACH"),
	HABILITACAO_ESTRANGEIRA(2, "Habilitação Estrangeira"),
	NAO_HABILITADO(3, "Não Habilitado"),
	NAO_INFORMADO(4, "Não Informado");
	
	private final Integer key;
	private final String value;
	
	TipoModeloCnhEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoModeloCnhEnum.values().length; index++) {
			if(key == TipoModeloCnhEnum.values()[index].getKey()) {
				return TipoModeloCnhEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
