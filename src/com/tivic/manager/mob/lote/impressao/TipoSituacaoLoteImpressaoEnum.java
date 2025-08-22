package com.tivic.manager.mob.lote.impressao;

public enum TipoSituacaoLoteImpressaoEnum {
	AGUARDANDO_GERACAO(-1, "Aguardando geração"),
	AGUARDANDO_IMPRESSAO(0, "Aguardando impressão"),
	EM_IMPRESSAO(1, "Em impressão"),
	IMPRESSO(2, "Impresso"),
	ENVELOPADO(3, "Envelopado"),
	EMBALADO(4, "Embalado"),
	ARQUIVO_CONSISTENTE(9, "Arquivo consistente"),
	ARQUIVO_INCONSISTENTE(10, "Arquivo inconsistente"),
	ECARTAS_ARQUIVO_AUTORIZADO(11, "E-cartas arquivo autorizado"),
	ECARTAS_ARQUIVO_NEGADO(12, "E-cartas arquivo negado"),
	REGISTRO_CANCELADO(13, "Registro cancelado");

	private final Integer key;
	private final String value;
	
	TipoSituacaoLoteImpressaoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoSituacaoLoteImpressaoEnum.values().length; index++) {
			if(key == TipoSituacaoLoteImpressaoEnum.values()[index].getKey()) {
				return TipoSituacaoLoteImpressaoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
