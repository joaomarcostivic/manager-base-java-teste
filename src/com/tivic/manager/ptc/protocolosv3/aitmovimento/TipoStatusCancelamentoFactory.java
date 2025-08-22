package com.tivic.manager.ptc.protocolosv3.aitmovimento;

import com.tivic.manager.mob.TipoStatusEnum;

public class TipoStatusCancelamentoFactory {
	public int strategy(int idTpDocumento) throws Exception {
		if (idTpDocumento == TipoStatusEnum.DEFESA_PREVIA.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_DEFESA_PREVIA.getKey();
		} 
		else if (idTpDocumento == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA.getKey();
		} 
		else if (idTpDocumento == TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_FICI.getKey();
		}
		else if (idTpDocumento == TipoStatusEnum.DEFESA_DEFERIDA.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_DEFESA_DEFERIDA.getKey();
		}
		else if (idTpDocumento == TipoStatusEnum.DEFESA_INDEFERIDA.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey();
		}
		else if (idTpDocumento == TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey();
		}
		else if (idTpDocumento == TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey();
		}
		else if (idTpDocumento == TipoStatusEnum.RECURSO_JARI .getKey()) {
			return TipoStatusEnum.CANCELAMENTO_RECURSO_JARI .getKey();
		}
		else if (idTpDocumento == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_JARI_COM_PROVIMENTO.getKey();
		}
		else if (idTpDocumento == TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_JARI_SEM_ACOLHIMENTO.getKey();
		}
		else if (idTpDocumento == TipoStatusEnum.RECURSO_CETRAN.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_RECURSO_CETRAN.getKey();
		}
		else {
			throw new Exception("Erro ao atualizar o Movimento, Tipo de Documento inválido.");
		}
	}
}
