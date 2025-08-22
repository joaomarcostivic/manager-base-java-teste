package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.adapter.base.antiga.loteimpressao.LoteImpressaoRepositoryOldDAO;
import com.tivic.manager.conf.ManagerConf;

public class LoteImpressaoFactory {
	
	private boolean lgBaseAntiga;
	
	public LoteImpressaoFactory() {
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");

	}
	
	public ILoteImpressaoRepository getStrategy() throws Exception {
		return this.lgBaseAntiga ? new LoteImpressaoRepositoryOldDAO() : new LoteImpressaoRepositoryDAO();
	}
}
