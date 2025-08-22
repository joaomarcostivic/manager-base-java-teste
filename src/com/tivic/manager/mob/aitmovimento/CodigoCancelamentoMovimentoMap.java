package com.tivic.manager.mob.aitmovimento;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.detran.mg.notificacao.TipoNotificacaoEnum;

public class CodigoCancelamentoMovimentoMap {

	private HashMap<Integer, Integer> codigoCancelamentoMap;
	
	public CodigoCancelamentoMovimentoMap() {
		this.codigoCancelamentoMap = new LinkedHashMap<Integer, Integer>();
		initCodigoCancelamentoMovimentacao();
	}
	
	private void initCodigoCancelamentoMovimentacao() {
		this.codigoCancelamentoMap.put(TipoStatusEnum.REGISTRO_INFRACAO.getKey(), TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.NAI_ENVIADO.getKey(), TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey(), TipoStatusEnum.CANCELAMENTO_FICI.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.DEFESA_PREVIA.getKey(), TipoStatusEnum.CANCELAMENTO_DEFESA_PREVIA.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.DEFESA_DEFERIDA.getKey(), TipoStatusEnum.CANCELAMENTO_DEFESA_DEFERIDA.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey(), TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.DEFESA_INDEFERIDA.getKey(), TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey(), TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.NIP_ENVIADA.getKey(), TipoStatusEnum.CANCELAMENTO_NIP.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.RECURSO_JARI.getKey(), TipoStatusEnum.CANCELAMENTO_RECURSO_JARI.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey(), TipoStatusEnum.CANCELAMENTO_JARI_COM_PROVIMENTO.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey(), TipoStatusEnum.CANCELAMENTO_JARI_SEM_ACOLHIMENTO.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.RECURSO_CETRAN.getKey(), TipoStatusEnum.CANCELAMENTO_RECURSO_CETRAN.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.FIM_PRAZO_DEFESA.getKey(), TipoStatusEnum.CANCELAMENTO_NIP.getKey());
		this.codigoCancelamentoMap.put(TipoStatusEnum.SUSPENSAO_DIVIDA_ATIVA.getKey(), TipoStatusEnum.CANCELAMENTO_DIVIDA_ATIVA.getKey());
	}
	
	public int get(int tpStatus) {
		if (tpStatus == TipoStatusEnum.REGISTRO_INFRACAO.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.REGISTRO_INFRACAO.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.NAI_ENVIADO.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.NAI_ENVIADO.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.DEFESA_PREVIA.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.DEFESA_PREVIA.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.DEFESA_DEFERIDA.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.DEFESA_DEFERIDA.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.DEFESA_INDEFERIDA.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.DEFESA_INDEFERIDA.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.NIP_ENVIADA.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.NIP_ENVIADA.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.RECURSO_JARI.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.RECURSO_JARI.getKey());
		}
		else if (tpStatus == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey());
		}
		else if (tpStatus == TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey());
		}
		else if (tpStatus == TipoStatusEnum.RECURSO_CETRAN.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.RECURSO_CETRAN.getKey());
		}
		else if (tpStatus == TipoStatusEnum.FIM_PRAZO_DEFESA.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
		} else if (tpStatus == TipoStatusEnum.SUSPENSAO_DIVIDA_ATIVA.getKey()) {
			return this.codigoCancelamentoMap.get(TipoStatusEnum.SUSPENSAO_DIVIDA_ATIVA.getKey());
		} else {
			return TipoNotificacaoEnum.MOVIMENTO_NAO_ESPECIFICADO.getKey();
		}
	}
	
}
