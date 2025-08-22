package com.tivic.manager.eCarta;

import com.tivic.manager.conf.ManagerConf;

public class EstrategiaGetParamns {
	public IGetParamns getEstrategiaParamns() {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		if (lgBaseAntiga) {
			return new GetParamnsBaseAntiga();
		}
		else {
			return new GetParamnsBaseNova();
		}
	}
}
