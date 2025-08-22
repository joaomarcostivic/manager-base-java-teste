package com.tivic.manager.grl.equipamento.base;

import com.tivic.manager.conf.ManagerConf;

public class EquipamentoServiceBaseFactory {
	public static EquipamentoServiceBase build() {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		if(lgBaseAntiga)
			return new EquipamentoServiceBaseAntiga();
		return new EquipamentoServiceBaseNova();
	}
}
