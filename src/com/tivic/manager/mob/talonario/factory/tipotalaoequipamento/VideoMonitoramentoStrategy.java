package com.tivic.manager.mob.talonario.factory.tipotalaoequipamento;

import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;

public class VideoMonitoramentoStrategy implements TipoTalaoEquipamentoStrategy {

	@Override
	public int getTpTalaoByEquipamento() {
		return TipoTalaoEnum.TP_TALONARIO_VIDEO_MONITORAMENTO.getKey();
	}
}
