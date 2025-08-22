package com.tivic.manager.mob.aitmovimento;

import com.tivic.manager.adapter.base.antiga.aitmovimento.AitMovimentoRepositoryOldDAO;
import com.tivic.manager.conf.ManagerConf;

public class AitMovimentoRepositoryFactory {
	private boolean lgBaseAntiga;
	
	public AitMovimentoRepositoryFactory() {
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	}
	
	public AitMovimentoRepository getStrategy() throws Exception {
		return this.lgBaseAntiga ? new AitMovimentoRepositoryOldDAO() : new AitMovimentoRepositoryDAO();
	}
}
