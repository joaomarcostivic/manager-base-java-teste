package com.tivic.manager.mob;

public enum TipoStatusEnum {

	CADASTRO_CANCELADO(AitMovimentoServices.CADASTRO_CANCELADO, "Cadastro Cancelado"),
	CADASTRADO(AitMovimentoServices.CADASTRADO, "Cadastrado"),
	ENVIADO_AO_DETRAN(AitMovimentoServices.ENVIADO_AO_DETRAN, "Enviado ao Detran"),
	REGISTRO_INFRACAO(AitMovimentoServices.REGISTRO_INFRACAO, "Registro de Infração"),
	NAI_ENVIADO(AitMovimentoServices.NAI_ENVIADO, "NAI Enviado"),
	AR_NAI(AitMovimentoServices.AR_NAI, "AR NAI"),
	NIP_ENVIADA(AitMovimentoServices.NIP_ENVIADA, "NIP Enviada"),
	AR_NIP(AitMovimentoServices.AR_NIP, "AR NIP"),
	DEFESA_PREVIA(AitMovimentoServices.DEFESA_PREVIA, "Defesa Prévia"),
	DEFESA_DEFERIDA(AitMovimentoServices.DEFESA_DEFERIDA, "Defesa Deferida"),
	DEFESA_INDEFERIDA(AitMovimentoServices.DEFESA_INDEFERIDA, "Defesa Indeferida"),
	RECURSO_JARI(AitMovimentoServices.RECURSO_JARI, "Recurso JARI"),
	JARI_COM_PROVIMENTO(AitMovimentoServices.JARI_COM_PROVIMENTO, "JARI com Provimento"),
	JARI_SEM_PROVIMENTO(AitMovimentoServices.JARI_SEM_PROVIMENTO, "JARI sem Provimento"),
	RECURSO_CETRAN(AitMovimentoServices.RECURSO_CETRAN, "Recurso CETRAN"),
	CETRAN_DEFERIDO(AitMovimentoServices.CETRAN_DEFERIDO, "CETRAN Deferido"),
	CETRAN_INDEFERIDO(AitMovimentoServices.CETRAN_INDEFERIDO, "CETRAN Indeferido"),
	PENALIDADE_SUSPENSA(AitMovimentoServices.PENALIDADE_SUSPENSA, "Penalidade Suspensa"),
	CANCELA_REGISTRO_MULTA(AitMovimentoServices.CANCELA_REGISTRO_MULTA, "Cancelamento Registro Multa"),
	PENALIDADE_REATIVADA(AitMovimentoServices.PENALIDADE_REATIVADA, "Penalidade Reativada"),
	TRANSFERENCIA_PONTUACAO(AitMovimentoServices.TRANSFERENCIA_PONTUACAO, "Transferência de Pontuação"),
	CANCELAMENTO_PONTUACAO(AitMovimentoServices.CANCELAMENTO_PONTUACAO, "Cancelamento Pontuação"),
	REGISTRA_PONTUACAO(AitMovimentoServices.REGISTRA_PONTUACAO, "Registro Pontuação"),
	SUSPENDE_PONTUACAO(AitMovimentoServices.SUSPENDE_PONTUACAO, "Suspensão Pontuação"),
	REATIVA_MULTA_PONTUACAO(AitMovimentoServices.REATIVA_MULTA_PONTUACAO, "Reativa Multa Pontuação"),
	MULTA_PAGA(AitMovimentoServices.MULTA_PAGA, "Multa Paga"),
	CANCELAMENTO_AUTUACAO(AitMovimentoServices.CANCELAMENTO_AUTUACAO, "Cancelamento Autuação"),
	CANCELAMENTO_MULTA(AitMovimentoServices.CANCELAMENTO_MULTA, "Cancelamento Multa"),
	MUDANCA_VENCIMENTO_NIP(AitMovimentoServices.MUDANCA_VENCIMENTO_NIP, "Mudança vencimento NIP"),
	SUSPENSAO_DIVIDA_ATIVA(AitMovimentoServices.SUSPENSAO_DIVIDA_ATIVA, "Suspensão Dívida Ativa"),
	DEVOLUCAO_PAGAMENTO(AitMovimentoServices.DEVOLUCAO_PAGAMENTO, "Devolução de Pagamento"),
	REATIVACAO(AitMovimentoServices.REATIVACAO, "Reativação"),
	MULTA_PAGA_OUTRA_UF(AitMovimentoServices.MULTA_PAGA_OUTRA_UF, "Multa paga em outra UF"),
	AR_RESULTADO(AitMovimentoServices.AR_RESULTADO, "AR Resultado"),
	DEVOLUCAO_SOLICITADA(AitMovimentoServices.DEVOLUCAO_SOLICITADA, "Devolução Solicitada"),
	DEVOLUCAO_NEGADA(AitMovimentoServices.DEVOLUCAO_NEGADA, "Devolução Negada"),
	RECALCULO_NIC(AitMovimentoServices.RECALCULO_NIC, "Recálculo NIC"),
	ATUALIZACAO_REGISTRO(AitMovimentoServices.ATUALIZACAO_REGISTRO, "Atualização Registro"),
	ADVERTENCIA_DEFESA_ENTRADA(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA, "Defesa Prévia (Advertência)"),
	ADVERTENCIA_DEFESA_DEFERIDA(AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA, "Defesa Deferida (Advertência)"),
	ADVERTENCIA_DEFESA_INDEFERIDA(AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA, "Defesa Indeferida (Advertência)"),
	REABERTURA_FICI(AitMovimentoServices.REABERTURA_FICI, "Reabertura Prazo (FICI)"),
	REABERTURA_DEFESA(AitMovimentoServices.REABERTURA_DEFESA, "Reabertura Prazo (Defesa Prévia)"),
	REABERTURA_JARI(AitMovimentoServices.REABERTURA_JARI, "Reabertura Prazo (JARI)"),
	CANCELAMENTO_DEFESA_PREVIA(AitMovimentoServices.CANCELAMENTO_DEFESA_PREVIA, "Cancelamento de Entrada de Defesa (Defesa Prévia)"),
	CANCELAMENTO_DEFESA_DEFERIDA(AitMovimentoServices.CANCELAMENTO_DEFESA_DEFERIDA, "Cancelamento de Defesa Deferida"),
	CANCELAMENTO_DEFESA_INDEFERIDA(AitMovimentoServices.CANCELAMENTO_DEFESA_INDEFERIDA, "Cancelamento de Defesa Indeferida"),
	CANCELAMENTO_FICI(AitMovimentoServices.CANCELAMENTO_TRANSFERENCIA_PONTUACAO, "Cancelamento de Transferência de Pontuação (FICI)"),
	CANCELAMENTO_RECURSO_JARI(AitMovimentoServices.CANCELAMENTO_RECURSO_JARI, "Cancelamento de Recurso na JARI (Entrada)"),
	CANCELAMENTO_JARI_COM_PROVIMENTO(AitMovimentoServices.CANCELAMENTO_JARI_COM_PROVIMENTO, "Cancelamento de Recurso na JARI com Provimento"),
	CANCELAMENTO_JARI_SEM_ACOLHIMENTO(AitMovimentoServices.CANCELAMENTO_JARI_SEM_PROVIMENTO, "Cancelamento de Recurso na JARI sem Provimento"),
	CANCELAMENTO_RECURSO_CETRAN(AitMovimentoServices.CANCELAMENTO_RECURSO_CETRAN, "Cancelamento de Recurso na CETRAN (Entrada)"),
	CANCELAMENTO_CETRAN_COM_PROVIMENTO(AitMovimentoServices.CANCELAMENTO_CETRAN_COM_PROVIMENTO, "Cancelamento de Recurso na CETRAN com Provimento"),
	CANCELAMENTO_CETRAN_SEM_ACOLHIMENTO(AitMovimentoServices.CANCELAMENTO_CETRAN_SEM_PROVIMENTO, "Cancelamento de Recurso na CETRAN sem Provimento"),
	CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA(AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_ENTRADA, "Cancelamento de Entrada de Defesa (Defesa Prévia)"),
	CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA(AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA, "Cancelamento de Defesa Deferida"),
	CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA(AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA, "Cancelamento de Defesa Indeferida"),
	CANCELAMENTO_NIP(AitMovimentoServices.CANCELAMENTO_NIP, "Cancelamento de Notificação de Penalidade (NIP)"),
	CANCELAMENTO_DIVIDA_ATIVA(AitMovimentoServices.CANCELAMENTO_DIVIDA_ATIVA, "Cancelamento de Dívida Ativa"),
	PUBLICACAO_NAI(AitMovimentoServices.PUBLICACAO_NAI, "Publicação de NAI em Diário Oficial"),
	PUBLICACAO_NIP(AitMovimentoServices.PUBLICACAO_NIP, "Publicação de NIP em Diário Oficial"),
	APRESENTACAO_CONDUTOR(AitMovimentoServices.APRESENTACAO_CONDUTOR, "Apresentação de condutor"), 
	FIM_PRAZO_DEFESA(AitMovimentoServices.FIM_PRAZO_DEFESA, "Fim do prazo de defesa da NAI"),
	SITUACAO_NAO_DEFINIDA(AitMovimentoServices.SITUACAO_NAO_DEFINIDA, "Situação do AIT não identificada"),
	PUBLICACAO_RESULTADO_JARI(AitMovimentoServices.PUBLICACAO_RESULTADO_JARI, "Publicação de resultado da JARI"),
	CANCELAMENTO_PUBLICACAO_RESULTADO_JARI(AitMovimentoServices.CANCELAMENTO_PUBLICACAO_RESULTADO_JARI, "Cancelamento de Publicação de resultado da JARI"),
	NOVO_PRAZO_DEFESA(AitMovimentoServices.NOVO_PRAZO_DEFESA, "NOVO PRAZO PARA ABERTURA DE DEFESA DA AUTUACAO"),
	NOVO_PRAZO_JARI(AitMovimentoServices.NOVO_PRAZO_JARI , "NOVO PRAZO PARA ABERTURA DE RECURSO JARI E PAGAMENTO COM DESCONTO"),
	CANCELAMENTO_CETRAN_SEM_PROVIMENTO(AitMovimentoServices.CANCELAMENTO_CETRAN_SEM_PROVIMENTO , "Cetran Indeferido Cancelado."),
	CANCELAMENTO_PAGAMENTO(AitMovimentoServices.CANCELAMENTO_PAGAMENTO, "Cancelamento de pagamento"),
	NIC_ENVIADA(AitMovimentoServices.NIC_ENVIADO, "NIC Enviada"),
	NOTIFICACAO_ADVERTENCIA(AitMovimentoServices.NOTIFICACAO_ADVERTENCIA, "Notificação de Advertência"),
	BAIXA_DESVINCULADA_LEILAO(AitMovimentoServices.BAIXA_DESVINCULADA_LEILAO, "Desvinculada – Hasta Publica (Leilão)"),
	CARTAO_IDOSO(AitMovimentoServices.CARTAO_IDOSO, "Cartão do Idoso"),
	CARTAO_PCD(AitMovimentoServices.CARTAO_PCD, "Cartão de Pessoa com Deficiência."),
	NIP_ESTORNADA(AitMovimentoServices.NIP_ESTORNADA, "Nip estornada"),
	ENVIOS_MULTIPLOS_RENAINF(AitMovimentoServices.ENVIOS_MULTIPLOS_RENAINF, "Envio a base nacional dos movimento: Registro de infração, NAI, NIP e Multa paga"),
	DADOS_CORREIO_NA(AitMovimentoServices.DADOS_CORREIO_NA, "Dados do correio NA"),
	DADOS_CORREIO_NP(AitMovimentoServices.DADOS_CORREIO_NP, "Dados do correio NP");
	
	private final Integer key;
	private final String value;
	
	TipoStatusEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoStatusEnum.values().length; index++) {
			if(key == TipoStatusEnum.values()[index].getKey()) {
				return TipoStatusEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}
