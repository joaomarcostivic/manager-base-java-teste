package com.tivic.manager.mob.aitmovimento;

import com.tivic.manager.mob.TipoStatusEnum;

public class DataMovimentoAVencerFactory {
	public IDataMovimentoAVencer getStrategy(int tpStatus) throws Exception {
		if (tpStatus == TipoStatusEnum.REGISTRO_INFRACAO.getKey()) {
			return new DataMovimentoAVencerRegistro();
		} else if (tpStatus == TipoStatusEnum.NAI_ENVIADO.getKey()) {
			return new DataMovimentoAVencerNAI();
		} else if (tpStatus == TipoStatusEnum.NIP_ENVIADA.getKey()) {
			return new DataMovimentoAVencerNIP();
		} else if (tpStatus == TipoStatusEnum.DEFESA_PREVIA.getKey() || tpStatus == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return new DataMovimentoAVencerDEFESA();
		} else if (tpStatus == TipoStatusEnum.RECURSO_JARI.getKey()) {
			return new DataMovimentoAVencerJARI();
		} else if (tpStatus == TipoStatusEnum.RECURSO_CETRAN.getKey()) {
			return new DataMovimentoAVencerCETRAN();
		} else if (tpStatus == TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey()) {
			return new DataMovimentoAVencerFICI();
		}
		return new DataMovimentoSemVencimento();
	}
}