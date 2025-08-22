package com.tivic.manager.mob.infracao;

import com.tivic.manager.adapter.base.antiga.infracao.InfracaoRepositoryOldDAO;
import com.tivic.manager.conf.ManagerConf;

public class InfracaoRepositoryFactory {
	private boolean lgBaseAntiga;
	
	public InfracaoRepositoryFactory() {
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");

	}
	
	public InfracaoRepository getStrategy() throws Exception {
		return this.lgBaseAntiga ? new InfracaoRepositoryOldDAO() : new InfracaoRepositoryDAO();
	}
}
