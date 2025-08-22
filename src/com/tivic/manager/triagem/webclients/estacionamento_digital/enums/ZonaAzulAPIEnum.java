package com.tivic.manager.triagem.webclients.estacionamento_digital.enums;

public enum ZonaAzulAPIEnum {

	PARE_AZUL(1, "API PareAzul"),
    SERBET(2, "Serbet API");

    private Integer key;
    private String value;

    private ZonaAzulAPIEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static String getValueByKey(int key) {
        for (int i = 0; i < ZonaAzulAPIEnum.values().length; i++) {
            if (key == ZonaAzulAPIEnum.values()[key].getKey()) {
                return ZonaAzulAPIEnum.values()[key].getValue();
            }
        }
        return "Tipo de API não definida.";
    }
	
}
