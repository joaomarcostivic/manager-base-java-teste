package com.tivic.manager.mob.orgao.enums;

public enum OrgaoAutuadorParametroEnum {
	OLD("cd_orgao_autuante"),
    NEW("MOB_CD_ORGAO_AUTUADOR");

	private final String value;

	OrgaoAutuadorParametroEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
