package com.tivic.manager.mob.lotes.enums.banco;

public enum BancoConvenioEnum {
	SEM_CONVENIO(0, "Sem Convenio com o banco"),
	COM_CONVENIO(1, "Conveniado ao banco");
	
	private final Integer key;
	private final String value;
	
	BancoConvenioEnum(Integer key, String value){
		this.key = key;
		this.value = value;
	}
	
	public Integer getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
}
