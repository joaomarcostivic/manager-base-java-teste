package com.tivic.manager.wsdl.detran.mg.notificacao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import com.tivic.manager.mob.TipoStatusEnum;

public class CodigoMovimentacaoMap {
	
	private HashMap<Integer, Integer> codigoMovimentacaoMap;
	
	public CodigoMovimentacaoMap() {
		this.codigoMovimentacaoMap = new LinkedHashMap<Integer, Integer>();
		initCodigoMovimentacao();
	}
	
	public int get(int tpStatus) {
		if (tpStatus == TipoStatusEnum.NAI_ENVIADO.getKey()) {
			return this.codigoMovimentacaoMap.get(TipoStatusEnum.NAI_ENVIADO.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.NIP_ENVIADA.getKey()) {
			return this.codigoMovimentacaoMap.get(TipoStatusEnum.NIP_ENVIADA.getKey());
		}
		else if (tpStatus == TipoStatusEnum.NOVO_PRAZO_DEFESA.getKey()) {
			return this.codigoMovimentacaoMap.get(TipoStatusEnum.NOVO_PRAZO_DEFESA.getKey());
		}
		else if (tpStatus == TipoStatusEnum.NOVO_PRAZO_JARI.getKey()) {
			return this.codigoMovimentacaoMap.get(TipoStatusEnum.NOVO_PRAZO_JARI.getKey());
		}
		else if (tpStatus == TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey()) {
			return this.codigoMovimentacaoMap.get(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
		}
		else {
			return TipoNotificacaoEnum.MOVIMENTO_NAO_ESPECIFICADO.getKey();
		}
	}
	
	public void initCodigoMovimentacao() {
		this.codigoMovimentacaoMap.put(TipoStatusEnum.NAI_ENVIADO.getKey(), TipoNotificacaoEnum.NOTIFICACAO_AUTUACAO.getKey());
		this.codigoMovimentacaoMap.put(TipoStatusEnum.NIP_ENVIADA.getKey(), TipoNotificacaoEnum.NOTIFICACAO_PENALIDADE.getKey());
		this.codigoMovimentacaoMap.put(TipoStatusEnum.NOVO_PRAZO_DEFESA.getKey(), TipoNotificacaoEnum.NOVO_PRAZO_DEFESA.getKey());
		this.codigoMovimentacaoMap.put(TipoStatusEnum.NOVO_PRAZO_JARI.getKey(), TipoNotificacaoEnum.NOVO_PRAZO_JARI.getKey());
		this.codigoMovimentacaoMap.put(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey(), TipoNotificacaoEnum.NOTIFICACAO_PENALIDADE_ADVERTENCIA.getKey());
	}
	
}
