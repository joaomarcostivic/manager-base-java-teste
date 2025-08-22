package com.tivic.manager.mob.ait;

import com.tivic.manager.adapter.base.antiga.ait.AitRepositoryOldDAO;
import com.tivic.manager.conf.ManagerConf;

public class AitRepositoryFactory {
	private boolean lgBaseAntiga;
	
	public AitRepositoryFactory() {
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	}
	
	public AitRepository getStrategy() throws Exception {
		return this.lgBaseAntiga ? new AitRepositoryOldDAO() : new AitRepositoryDAO();
	}
	
}
