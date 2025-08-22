package com.tivic.manager.mob.aitmovimento;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.sol.cdi.BeansFactory;

public class DataMovimentoAVencerNIP implements IDataMovimentoAVencer {

	private static final int PRAZO_SEIS_MESES = 180;
	private static final int PRAZO_DOZE_MESES = 360;
	private AitRepository aitRepository;
	private IAitMovimentoService aitMovimentoServices;

	public DataMovimentoAVencerNIP() throws Exception {
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public GregorianCalendar calcularVencimento(int cdAit) throws Exception {
		Ait ait = aitRepository.get(cdAit);
		int tempoParaEmissao = aitMovimentoServices.getAllDefesas(ait.getCdAit())
				.isEmpty() ? PRAZO_SEIS_MESES : PRAZO_DOZE_MESES;
		GregorianCalendar prazoEmissao = ait.getDtInfracao();
		prazoEmissao.set(Calendar.AM_PM, 0);
		prazoEmissao.set(Calendar.HOUR, 0);
		prazoEmissao.set(Calendar.MINUTE, 0);
		prazoEmissao.set(Calendar.SECOND, 0);
		prazoEmissao.set(Calendar.MILLISECOND, 0);
		prazoEmissao.add(Calendar.DATE, tempoParaEmissao);
		return prazoEmissao;
	}

}
