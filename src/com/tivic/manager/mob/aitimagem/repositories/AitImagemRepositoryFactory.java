package com.tivic.manager.mob.aitimagem.repositories;

import com.tivic.manager.adapter.base.antiga.aitImagem.AitImagemRepositoryOldDAO;
import com.tivic.manager.conf.ManagerConf;

public class AitImagemRepositoryFactory {
	private boolean lgBaseAntiga;
	
	public AitImagemRepositoryFactory() {
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	}
	
	public IAitImagemRepository getStrategy() throws Exception {
		return this.lgBaseAntiga ? new AitImagemRepositoryOldDAO() : new AitImagemRepositoryDAO();
	}
}
