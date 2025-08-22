package com.tivic.manager.mob.talonario;

import com.tivic.manager.adapter.base.antiga.talonario.TalonarioRepositoryOldDAO;
import com.tivic.manager.conf.ManagerConf;

public class TalonarioRepositoryFactory {
	private boolean lgBaseAntiga;
	
	public TalonarioRepositoryFactory() {
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");

	}
	
	public TalonarioRepository getStrategy() throws Exception {
		return this.lgBaseAntiga ? new TalonarioRepositoryOldDAO() : new TalonarioRepositoryDAO();
	}
}
