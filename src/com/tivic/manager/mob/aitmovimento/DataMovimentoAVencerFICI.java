package com.tivic.manager.mob.aitmovimento;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.sol.cdi.BeansFactory;

public class DataMovimentoAVencerFICI implements IDataMovimentoAVencer {
	private AitRepository aitRepository;

	public DataMovimentoAVencerFICI() throws Exception {
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public GregorianCalendar calcularVencimento(int cdAit) throws Exception {
		Ait ait = aitRepository.get(cdAit);
		GregorianCalendar dataVencimento = ait.getDtPrazoDefesa();
		if(dataVencimento != null) {
			dataVencimento.set(Calendar.AM_PM, 0);
			dataVencimento.set(Calendar.HOUR, 0);
			dataVencimento.set(Calendar.MINUTE, 0);
			dataVencimento.set(Calendar.SECOND, 0);
			dataVencimento.set(Calendar.MILLISECOND, 0);
		}
		return dataVencimento;
	}
}
