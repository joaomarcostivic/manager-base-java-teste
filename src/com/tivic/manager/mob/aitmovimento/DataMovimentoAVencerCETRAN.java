package com.tivic.manager.mob.aitmovimento;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.sol.cdi.BeansFactory;

public class DataMovimentoAVencerCETRAN implements IDataMovimentoAVencer{
	
	private static final int PRAZO_TRINTA_DIAS = 30;
	private AitRepository aitRepository;

	public DataMovimentoAVencerCETRAN() throws Exception {
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}
	
	@Override
	public GregorianCalendar calcularVencimento(int cdAit) throws Exception {
		Ait ait = aitRepository.get(cdAit);
		GregorianCalendar prazo = ait.getDtVencimento();
		prazo.set(Calendar.AM_PM, 0);
		prazo.set(Calendar.HOUR, 0);
		prazo.set(Calendar.MINUTE, 0);
		prazo.set(Calendar.SECOND, 0);
		prazo.add(Calendar.DATE, PRAZO_TRINTA_DIAS);
		prazo.set(Calendar.MILLISECOND, 0);
		return prazo;
	}

}
