package com.tivic.manager.mob.lote.impressao.enums;

public enum LoteImpressaoSituacaoEnum {
    AGUARDANDO_ENVIO(0, "Aguardando envio"),
    ENVIADO(1, "Enviado"),
    EM_IMPRESSAO(2, "Em impressão"),
    EM_ENVELOPAMENTO(3, "Em envelopamento"),
    EMBALANDO(4, "Embalando"),
    ENCAMINHADO_TRASPORTE(5, "Encaminhado para transporte"),
    EM_TRANSPORTE(6, "Em transporte"),
    RECEBIDO(7, "Recebido"),
    ENVIADO_eCARTAS_AGUARDANDO_RESPOSTA(8, "eCartas - Enviado e aguardando resposta"),
    ECARTAS_LOTE_CONSISTENTE(9, "eCartas - Lote consistente"),
    ECARTAS_LOTE_INCONSISTENTE(10, "eCartas - Lote inconsistente"),
    ECARTAS_LOTE_INVALIDO(11, "eCartas - Lote inválido"),
    ECARTAS_LOTE_AUTORIZADO(12, "eCartas - Lote autorizado"),
    ECARTAS_LOTE_REJEITADO(13, "eCartas - Lote rejeitado"),
    AGUARDANDO_IMPRESSAO(14, "Aguardando impressão"),
    AGUARDANDO_MOVIMENTOS(15, "Aguardando movimentos"),
    REMESSA_DETRAN_ERRO(16, "Remessa Detran - Erro"),
    NAO_ENVIADO_DETRAN(17, "Não enviado Detran"),
    AGUARDANDO_DOCUMENTOS(18, "Aguardando documentos"),
    ECARTAS_SOLICITAR_CANCELAMENTO(19, "eCartas - Solicitar cancelamento"),
    ARQUIVO_PUBLICACAO_DIARIO_OFICIAL_GERADO(20, "Arquivo para publicação Diário Oficial - Gerado"),
    ARQUIVO_PUBLICACAO_DIARIO_OFICIAL_ENVIADO(21, "Arquivo para publicação Diário Oficial - Enviado"),
    ECARTAS_LOTE_COM_ERRO(22, "eCartas - Lote com erro"),
    ARQUIVO_SERVICO_ECARTAS_DISPONIVEL(23, "eCartas - Arquivo de serviço disponível para download");

	private final Integer key;
	private final String value;
	
	LoteImpressaoSituacaoEnum(Integer key, String value){
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
		for(Integer index=0; index < LoteImpressaoSituacaoEnum.values().length; index++) {
			if(key == LoteImpressaoSituacaoEnum.values()[index].getKey()) {
				return LoteImpressaoSituacaoEnum.values()[index].getValue();
			}
		}
		return null;
	}
}
