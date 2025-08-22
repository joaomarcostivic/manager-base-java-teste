package com.tivic.manager.seg.usuario.repositories;

import com.tivic.manager.adapter.base.antiga.usuario.UsuarioRepositoryOldDAO;
import com.tivic.manager.conf.ManagerConf;

public class UsuarioRepositoryFactory {
private boolean lgBaseAntiga;
	
	public UsuarioRepositoryFactory() {
		this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	}
	
	public IUsuarioRepository getStrategy() throws Exception {
		return this.lgBaseAntiga ? new UsuarioRepositoryOldDAO() : new UsuarioRepositoryDAO();
	}
}
