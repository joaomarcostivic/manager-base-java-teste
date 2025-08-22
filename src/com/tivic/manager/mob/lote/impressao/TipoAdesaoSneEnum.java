package com.tivic.manager.mob.lote.impressao;

public enum TipoAdesaoSneEnum {
	SEM_OPCAO_SNE(0, "Sem opção ao SNE"),
	COM_OPCAO_SNE(1, "Com opção ao SNE"),
	CANCELAMENTO_OPCAO_SNE(2, "Cancelamento de opção ao SNE");
	
	private final Integer key;
	private final String value;
	
	TipoAdesaoSneEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoAdesaoSneEnum.values().length; index++) {
			if(key == TipoAdesaoSneEnum.values()[index].getKey()) {
				return TipoAdesaoSneEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
