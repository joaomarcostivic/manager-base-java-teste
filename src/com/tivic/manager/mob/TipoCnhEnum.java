package com.tivic.manager.mob;

import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;

public enum TipoCnhEnum {
	
	NAO_INFORMADO(-1, "Não Informado"),
	TP_CNH_PGU(TabelasAuxiliaresMG.TP_CNH_PGU, "Antiga (PGU)"),
	TP_CNH_RENACH(TabelasAuxiliaresMG.TP_CNH_RENACH, "Renach"),
	TP_CNH_HABILITACAO_ESTRANGEIRA(TabelasAuxiliaresMG.TP_CNH_HABILITACAO_ESTRANGEIRA, "CNH Estrangeira"),
	TP_CNH_NAO_HABILITADO(TabelasAuxiliaresMG.TP_CNH_NAO_HABILITADO, "Não Habilitado");
	
	private final Integer key;
	private final String value;
	
	TipoCnhEnum(Integer key, String value) {
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
		for(Integer index = 0; index < TipoCnhEnum.values().length; index++) {
			if(key == TipoCnhEnum.values()[index].getKey())
				return TipoCnhEnum.values()[index].getValue();
		}
		
		return null;
	}

}
