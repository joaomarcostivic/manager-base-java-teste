package com.tivic.manager.str.ait;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.str.Ait;

public class AitDuplicidadeDatabaseFactory {
	
	public AitDuplicidadeDatabaseFactory() {}
	
	public IAitVerificadorDuplicidade verificador() {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		return lgBaseAntiga ? new AitVerificadorDuplicidadeBaseAntiga() : new AitVerificadorDuplicidadeBaseNova();
	}

}
