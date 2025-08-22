package com.tivic.manager.grl.parametro.repository;

import com.tivic.manager.adapter.base.antiga.parametro.ParametroOldRepositoryDAO;
import com.tivic.manager.conf.ManagerConf;

public class ParametroRepositoryDAOFactory {
	
	boolean lgBaseAntiga;
	
	public ParametroRepositoryDAOFactory() {
		lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	}
	
	public IParametroRepository getStrategy() throws Exception {
		return lgBaseAntiga ? new ParametroOldRepositoryDAO() : new ParametroRepositoryDAO();
	}
}
