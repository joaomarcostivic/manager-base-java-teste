package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class LoteNotificacaoAitService implements ILoteNotificacaoAitService{
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	
	public LoteNotificacaoAitService() throws Exception {
		loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
	}

	public LoteImpressaoAit save(LoteImpressaoAit loteImpressaoAit) throws Exception {
		return save(loteImpressaoAit, new CustomConnection());
	}
	
	public LoteImpressaoAit save(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			if(loteImpressaoAit==null)
				throw new ValidacaoException("O Lote de AIT não pode ser nulo.");
			if(loteImpressaoAit.getCdAit()==0)
				loteImpressaoAitRepository.insert(loteImpressaoAit, customConnection);
			else 
				loteImpressaoAitRepository.update(loteImpressaoAit, customConnection);
			customConnection.finishConnection();
			return loteImpressaoAit;
		}
		finally{
			customConnection.closeConnection();
		}
	}

}
