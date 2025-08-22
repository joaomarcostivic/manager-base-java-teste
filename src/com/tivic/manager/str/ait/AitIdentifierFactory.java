package com.tivic.manager.str.ait;

import java.sql.Connection;

import com.tivic.manager.conf.ManagerConf;

public class AitIdentifierFactory {
	
	public AitIdentifierFactory() {}
	
	public IAitVerificadorDuplicidade verificador(String aitIdentifier, Connection connection) throws Exception {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		return lgBaseAntiga ? getStrategyByNrAit(Integer.valueOf(aitIdentifier), connection) : getStrategyByIdAit(aitIdentifier, connection);
	}
	
	private IAitVerificadorDuplicidade getStrategyByIdAit(String idAit, Connection connection) throws Exception {
	    com.tivic.manager.mob.Ait ait = new com.tivic.manager.mob.Ait();
	    ait.setIdAit(idAit);
	    return new AitVerificadorDuplicidadeBaseNova().findByIdAit(ait, connection);
	}

	private IAitVerificadorDuplicidade getStrategyByNrAit(int nrAit, Connection connection) throws Exception {
	    com.tivic.manager.str.Ait ait = new com.tivic.manager.str.Ait();
	    ait.setNrAit(nrAit);
	    return new AitVerificadorDuplicidadeBaseAntiga().findByNrAit(ait, connection);
	}

}
