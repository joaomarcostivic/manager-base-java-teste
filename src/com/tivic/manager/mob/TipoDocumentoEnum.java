package com.tivic.manager.mob;

import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;

public enum TipoDocumentoEnum {
	
	TP_DOCUMENTO_CPF(TabelasAuxiliaresMG.TP_DOCUMENTO_CPF, "CPF"),
	TP_DOCUMENTO_CNPJ(TabelasAuxiliaresMG.TP_DOCUMENTO_CNPJ, "CNPJ"),
	TP_DOCUMENTO_RG(TabelasAuxiliaresMG.TP_DOCUMENTO_RG, "RG"),
	TP_DOCUMENTO_DOC_ESTRANGEIRO(TabelasAuxiliaresMG.TP_DOCUMENTO_DOC_ESTRANGEIRO, "Documento Estrangeiro"),
	TP_DOCUMENTO_OUTROS(TabelasAuxiliaresMG.TP_DOCUMENTO_OUTROS, "Outros"),
	TP_DOCUMENTO_NAO_APRESENTOU(TabelasAuxiliaresMG.TP_DOCUMENTO_NAO_APRESENTOU, "Não Apresentado");
	
	private final Integer key;
	private final String value;
	
	TipoDocumentoEnum(Integer key, String value) {
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
		for(Integer index = 0; index < TipoDocumentoEnum.values().length; index++) {
			if(key == TipoDocumentoEnum.values()[index].getKey())
				return TipoDocumentoEnum.values()[index].getValue();
		}
		
		return null;
	}

}
