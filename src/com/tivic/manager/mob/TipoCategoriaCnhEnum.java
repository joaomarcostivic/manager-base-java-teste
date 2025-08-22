package com.tivic.manager.mob;

public enum TipoCategoriaCnhEnum {

	CATEGORIA_A(0, "A"),
    CATEGORIA_B(1, "B"),
    CATEGORIA_AB(2, "AB"),
    CATEGORIA_C(3, "C"),
    CATEGORIA_AC(4, "AC"),
    CATEGORIA_D(5, "D"),
    CATEGORIA_AD(6, "AD"),
    CATEGORIA_E(7, "E"),
    CATEGORIA_AE(8, "AE");
	
	private final Integer key;
	private final String value;
	
	TipoCategoriaCnhEnum(Integer key, String value) {
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
		for(Integer index = 0; index < TipoCategoriaCnhEnum.values().length; index++) {
			if(key == TipoCategoriaCnhEnum.values()[index].getKey())
				return TipoCategoriaCnhEnum.values()[index].getValue();
		}
		
		return null;
	}
	
}
