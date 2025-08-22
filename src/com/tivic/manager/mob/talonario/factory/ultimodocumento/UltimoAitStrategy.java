package com.tivic.manager.mob.talonario.factory.ultimodocumento;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class UltimoAitStrategy implements UltimoNrDocumentoStrategy {
	private AitRepository aitRepository;
	private Talonario talonario;
	
	public UltimoAitStrategy(Talonario talonario) throws Exception  {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.talonario = talonario;
	}
	
    @Override
    public int getUltimoNrDocumento(CustomConnection customConnection) throws Exception {
        return aitRepository.getUltimoNrAitByTalao(talonario, customConnection);
    }
}
