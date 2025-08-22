package com.tivic.manager.mob.correios;

public enum ArquivoTipoEnum {

	CNH(4, "CNH"),
    ATESTADO_MEDICO(5, "Atestado médico"),
    ANTECEDENTES_CRIMINAIS(6, "Antecedentes criminais"),
    COMPROVANTE_RESIDENCIA(7, "Comprovante de residência"),
    ATESTADO_ATIVIDADE_REMUNERADA(8, "Atestado de atividade remunerada"),
    CARTEIRA_CONDUTOR(9, "Carteira condutor"),
    EMAIL(10, "E-MAIL"),
    REMESSA_REGISTRO_MULTAS(11, "Remessa registro de multas"),
    RETORNO_REGISTRO_MULTAS(12, "Retorno registro de multas"),
    REMESSA_CORREIOS(13, "Remessa correios"),
    RETORNO_CORREIOS(14, "Retorno correios"),
    REMESSA_BANCO(15, "Remessa banco"),
    RETORNO_BANCO(16, "Retorno banco"),
    REMESSA_COBRANCA(17, "Remessa cobrança"),
    RETORNO_COBRANCA(18, "Retorno cobrança"),
    NA_PUBLICADA_DIARIO_OFICIAL(19, "NA publicada no diário oficial"),
    NP_PUBLICADA_DIARIO_OFICIAL(20, "NP publicada no diário oficial"),
    PROCESSO_PUBLICADO_DIARIO_OFICIAL(21, "Processo publicado no diário oficial"),
    REMESSA_CORREIO_TEMP(-13, "Remssa correio temp"),
    RETORNO_CORREIO_TEMP(-14, "Retorno correio temp"),
    PUBLICACAO_PROTOCOLOS(22, "Publicação de protocolos"),
    LISTA_POSTAGEM_CORREIOS(23, "Lista postagem correios"),
    PUBLICACAO_NOTIFICACOES(24, "Publicação de notificações");
	
	private final Integer key;
	private final String value;
	
	ArquivoTipoEnum(Integer key, String value){
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
		for(Integer index=0; index < ArquivoTipoEnum.values().length; index++) {
			if(key == ArquivoTipoEnum.values()[index].getKey()) {
				return ArquivoTipoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
