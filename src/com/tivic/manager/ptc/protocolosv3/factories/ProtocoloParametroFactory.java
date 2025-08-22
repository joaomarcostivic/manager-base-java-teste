package com.tivic.manager.ptc.protocolosv3.factories;

import com.tivic.manager.adapter.base.antiga.parametro.ParametroOldRepositoryDAO;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;

public class ProtocoloParametroFactory {
	
	boolean lgBaseAntiga;
	boolean lgMigracao;
	
	public ProtocoloParametroFactory() {
		lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		lgMigracao = ManagerConf.getInstance().getAsBoolean("STR_MIGRACAO");
	}
	
	public IParametroRepository getStrategy() throws Exception {
		return lgBaseAntiga && !lgMigracao ? new ParametroOldRepositoryDAO() : new ParametroRepositoryDAO();
	}

}
