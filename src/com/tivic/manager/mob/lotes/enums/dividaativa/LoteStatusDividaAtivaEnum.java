package com.tivic.manager.mob.lotes.enums.dividaativa;

public enum LoteStatusDividaAtivaEnum {
	
	GERADO(0, "Gerado"),
	ENVIADO_AO_DETRAN(1, "Enviado ao Detran"),
	PENDENTE_COM_ERRO(2, "Pendente com Erro"),
	FINALIZADO(3, "Finalizado");

	private final Integer key;
	private final String value;
	
	LoteStatusDividaAtivaEnum(Integer key, String value){
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
		for(Integer index=0; index < LoteStatusDividaAtivaEnum.values().length; index++) {
			if(key == LoteStatusDividaAtivaEnum.values()[index].getKey()) {
				return LoteStatusDividaAtivaEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
