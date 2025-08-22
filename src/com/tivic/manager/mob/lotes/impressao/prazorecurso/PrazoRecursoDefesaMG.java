package com.tivic.manager.mob.lotes.impressao.prazorecurso;

import java.util.GregorianCalendar;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class PrazoRecursoDefesaMG implements IPrazoRecurso {
	private IAitService aitService;
	private AitRepository aitRepository; 
	
	public PrazoRecursoDefesaMG() throws Exception {
		aitService = (IAitService) BeansFactory.get(IAitService.class);
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public GregorianCalendar gerarPrazo(int cdAit, CustomConnection customConnection) throws Exception {
		Ait ait = aitRepository.get(cdAit, customConnection);
		aitService.updateDetran(ait, customConnection);
		if (ait.getDtPrazoDefesa() == null) {
			throw new ValidacaoException("Não foi possivel obter a data limite para defesa.");
		}
		return ait.getDtPrazoDefesa();
	}
}
