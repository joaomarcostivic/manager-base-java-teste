package com.tivic.manager.grl.banco;

public enum TipoBancoConvenioEnum {
	SEM_CONVENIO(0, "Sem Convenio com o banco"),
	COM_CONVENIO(1, "Conveniado ao banco");
	
	private final Integer key;
	private final String value;
	
	TipoBancoConvenioEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoBancoConvenioEnum.values().length; index++) {
			if(key == TipoBancoConvenioEnum.values()[index].getKey()) {
				return TipoBancoConvenioEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
