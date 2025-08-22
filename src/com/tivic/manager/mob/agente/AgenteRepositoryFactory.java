package com.tivic.manager.mob.agente;

import com.tivic.manager.adapter.base.antiga.agente.AgenteRepositoryOldDAO;
import com.tivic.manager.conf.ManagerConf;

public class AgenteRepositoryFactory {
	private boolean lgBaseAntiga;
	
	public AgenteRepositoryFactory() {
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");

	}
	
	public AgenteRepository getStrategy() throws Exception {
		return this.lgBaseAntiga ? new AgenteRepositoryOldDAO() : new AgenteRepositoryDAO();
	}
}
