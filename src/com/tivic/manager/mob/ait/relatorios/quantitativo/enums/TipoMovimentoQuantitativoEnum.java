package com.tivic.manager.mob.ait.relatorios.quantitativo.enums;

public enum TipoMovimentoQuantitativoEnum {

	NOTIFICACOES_ATUACOES_PROCESSADAS(3, "NOTIFICAÇÕES DE AUTUAÇÕES PROCESSADAS"),
	NOTIFICACOES_PENALIDADE_PROCESSADAS(5, "NOTIFICAÇÕES DE PENALIDADES PROCESSADAS"), 
	NOTIFICACOES_NIC_PROCESSADAS(91, "NOTIFICAÇÕES NIC PROCESSADAS"),
	DEFESAS_PREVIA_AUTUACAO_PROTOCOLOCADA(7, "DEFESAS DE AUTUAÇÃO PROTOCOLADAS"),
	DEFESAS_ADVERTENCIA_AUTUACAO_PROTOCOLOCADA(104, "DEFESAS COM ADVERTÊNCIA DE AUTUAÇÃO PROTOCOLADAS"),
	JARI_PROTOCOLADOS(10, "JARI PROTOCOLADOS"),
	CETRAN_PROTOCOLOCADOS(51, "CETRAN PROTOCOLADOS"),
	RECEBIMENTO_PENALIDADE_MULTA_PROCESSADAS_PERIODO(124, "RECEBIMENTO DE PENALIDADES DE MULTA PROCESSADAS NO PERÍODO"),
	A_RECEBER_PENALIDADES_MULTAS_PROCESSADAS(125, "A RECEBER DAS PENALIDADES DE MULTA PROCESSADAS"),
	RECEBIMENTOS_REALIZADOS_PERIODO(24, "RECEBIMENTOS REALIZADOS NO PERÍODO");

	private final Integer key;
	private final String value;
	
	TipoMovimentoQuantitativoEnum(Integer key, String value){
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
		for(Integer index=0; index < TipoMovimentoQuantitativoEnum.values().length; index++) {
			if(key == TipoMovimentoQuantitativoEnum.values()[index].getKey()) {
				return TipoMovimentoQuantitativoEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}