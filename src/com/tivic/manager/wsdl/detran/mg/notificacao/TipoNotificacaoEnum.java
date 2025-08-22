package com.tivic.manager.wsdl.detran.mg.notificacao;

public enum TipoNotificacaoEnum {

	MOVIMENTO_NAO_ESPECIFICADO(-1, "Movimento não especificado"),
	NOTIFICACAO_AUTUACAO(1, "NAI (EMISSAO DA NOTIFICACAO DA AUTUACAO)"),
	DADOS_CORREIO_NOTIFICACAO_AUTUACAO(2, "DADOS DE CORREIO DA NOTIFICACAO DA AUTUACAO"),
	PUBLICACAO_AUTUACAO(3, "PUBLICACAO DA AUTUACAO"),
	NOTIFICACAO_PENALIDADE(4, "EMISSAO DA NOTIFICACAO DA PENALIDADE"),
	DADOS_CORREIO_NOTIFICACAO_PENALIDADE(5, "DADOS DE CORREIO DA NOTIFICACAO DA PENALIDADE"),
	PUBLICACAO_PENALIDADE(6, "PUBLICACAO DA PENALIDADE"),
	NOVO_PRAZO_FICI(7, "NOVO PRAZO PARA RECEBIMENTO DE FICI"),
	NOVO_PRAZO_DEFESA(8, "NOVO PRAZO PARA ABERTURA DE DEFESA DA AUTUACAO"),
	NOVO_PRAZO_JARI(9, "NOVO PRAZO PARA ABERTURA DE RECURSO JARI E PAGAMENTO COM DESCONTO"),
	NOTIFICACAO_PENALIDADE_ADVERTENCIA(10, "EMISSAO DA NOTIFICACAO DE PENALIDADE DE ADVERTENCIA"),
	DADOS_CORREIO_NOTIFICACAO_PENALIDADE_ADVERTENCIA(11, "DADOS DE CORREIO DA NOTIFICACAO DE PENALIDADE DE ADVERTENCIA"),
	PUBLICACAO_PENALIDADE_ADVERTENCIA(12, "PUBLICACAO DA PENALIDADE DE ADVERTENCIA");
	
	private final Integer key;
	private final String value;
	
	TipoNotificacaoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoNotificacaoEnum.values().length; index++) {
			if(key == TipoNotificacaoEnum.values()[index].getKey()) {
				return TipoNotificacaoEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}
