package com.tivic.manager.mob.lotes.impressao.prazorecurso;

import java.util.Calendar;
import java.util.GregorianCalendar;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.sol.cdi.BeansFactory;

public class PrazoRecursoDefesaBA implements IPrazoRecurso {
	private AitRepository aitRepository;
	
	public PrazoRecursoDefesaBA() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public GregorianCalendar gerarPrazo(int cdAit, CustomConnection customConnection) throws Exception {
		GregorianCalendar dtPrazoDefesa = new GregorianCalendar();
		dtPrazoDefesa.add(Calendar.DATE, + ParametroServices.getValorOfParametroAsInteger("MOB_PRAZOS_NR_DEFESA_PREVIA", 30));
		Ait ait = aitRepository.get(cdAit, customConnection);
		ait.setDtPrazoDefesa(dtPrazoDefesa);
		this.aitRepository.update(ait, customConnection);
		return dtPrazoDefesa;
	}

}
