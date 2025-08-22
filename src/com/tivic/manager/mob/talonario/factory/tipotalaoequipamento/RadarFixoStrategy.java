package com.tivic.manager.mob.talonario.factory.tipotalaoequipamento;

import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;

public class RadarFixoStrategy implements TipoTalaoEquipamentoStrategy {

	@Override
	public int getTpTalaoByEquipamento() {
		return TipoTalaoEnum.TP_TALONARIO_RADAR_FIXO.getKey();
	}
}
