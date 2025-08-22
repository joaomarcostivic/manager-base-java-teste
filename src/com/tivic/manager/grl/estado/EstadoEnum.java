package com.tivic.manager.grl.estado;

public enum EstadoEnum {

	BAHIA(1, "BA"),
    SERGIPE(2, "SE"),
    ALAGOAS(3, "AL"),
    SAO_PAULO(4, "SP"),
    MINAS_GERAIS(5, "MG"),
    RIO_GRANDE_DO_NORTE(10, "RN"),
    RONDONIA(11, "RO"),
    ACRE(12, "AC"),
    AMAZONAS(13, "AM"),
    RORAIMA(14, "RR"),
    PARA(15, "PA"),
    AMAPA(16, "AP"),
    TOCANTINS(17, "TO"),
    MARANHAO(18, "MA"),
    PIAUI(19, "PI"),
    CEARA(20, "CE"),
    PARAIBA(21, "PB"),
    PERNAMBUCO(22, "PE"),
    ESPIRITO_SANTO(23, "ES"),
    RIO_DE_JANEIRO(24, "RJ"),
    PARANA(25, "PR"),
    SANTA_CATARINA(26, "SC"),
    RIO_GRANDE_DO_SUL(27, "RS"),
    MATO_GROSSO_DO_SUL(28, "MS"),
    MATO_GROSSO(29, "MT"),
    GOIAS(30, "GO"),
    DISTRITO_FEDERAL(31, "DF"),
    EX(32, "EX");

	private final Integer key;
	private final String value;

	EstadoEnum(Integer key, String value) {
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
		for(Integer index = 0; index < EstadoEnum.values().length; index++) {
			if(key == EstadoEnum.values()[index].getKey())
				return EstadoEnum.values()[index].getValue();
		}
		return null;
	}
	
}
