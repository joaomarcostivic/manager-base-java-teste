package com.tivic.manager.ptc.protocolosv3.statusmap;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.detran.mg.notificacao.TipoNotificacaoEnum;

public class ProtocoloToTipoResultado {
	private HashMap<Integer, Integer> codigoProtocoloMap;
	
	public ProtocoloToTipoResultado() {
		this.codigoProtocoloMap = new LinkedHashMap<Integer, Integer>();
	}
	
	public int getDeferimento(int tpStatus) {
		initCodigoMovimentacaoDeferida();
		if (tpStatus == TipoStatusEnum.DEFESA_PREVIA.getKey()) {
			return this.codigoProtocoloMap.get(TipoStatusEnum.DEFESA_PREVIA.getKey());
		} else if (tpStatus == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return this.codigoProtocoloMap.get(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey());
		} else if (tpStatus == TipoStatusEnum.RECURSO_CETRAN.getKey()) {
			return this.codigoProtocoloMap.get(TipoStatusEnum.RECURSO_CETRAN.getKey());
		} else {
			return TipoNotificacaoEnum.MOVIMENTO_NAO_ESPECIFICADO.getKey();
		}
	}
	
	public int getIndeferimento(int tpStatus) {
		initCodigoMovimentacaoIndeferida();
		if (tpStatus == TipoStatusEnum.DEFESA_PREVIA.getKey()) {
			return this.codigoProtocoloMap.get(TipoStatusEnum.DEFESA_PREVIA.getKey());
		} else if (tpStatus == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return this.codigoProtocoloMap.get(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey());
		} else if (tpStatus == TipoStatusEnum.RECURSO_CETRAN.getKey()) {
			return this.codigoProtocoloMap.get(TipoStatusEnum.RECURSO_CETRAN.getKey());
		} else {
			return TipoNotificacaoEnum.MOVIMENTO_NAO_ESPECIFICADO.getKey();
		}
	}
	
	public void initCodigoMovimentacaoDeferida() {
		this.codigoProtocoloMap.put(TipoStatusEnum.DEFESA_PREVIA.getKey(), TipoStatusEnum.DEFESA_DEFERIDA.getKey());
		this.codigoProtocoloMap.put(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey());
		this.codigoProtocoloMap.put(TipoStatusEnum.RECURSO_CETRAN.getKey(), TipoStatusEnum.CETRAN_DEFERIDO.getKey());
	}
	
	public void initCodigoMovimentacaoIndeferida() {
		this.codigoProtocoloMap.put(TipoStatusEnum.DEFESA_PREVIA.getKey(), TipoStatusEnum.DEFESA_INDEFERIDA.getKey());
		this.codigoProtocoloMap.put(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey());
		this.codigoProtocoloMap.put(TipoStatusEnum.RECURSO_CETRAN.getKey(), TipoStatusEnum.CETRAN_INDEFERIDO.getKey());
	}
}
