package com.tivic.manager.mob.processamento.sincronizacao;

import java.util.Arrays;

public class OrgaoQuerySelector {
	private final String CD_ORGAO_ITAUNA = "246750";
	private final String[] CODIGOS_ORGAOS_SERIAL = {CD_ORGAO_ITAUNA};
	
	public boolean isSerialId(String cdOrgao) {
        return Arrays.stream(CODIGOS_ORGAOS_SERIAL)
                     .anyMatch(codigoOrgao -> codigoOrgao.equals(cdOrgao));
    }
}
