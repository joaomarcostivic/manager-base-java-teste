package com.tivic.manager.mob.aitmovimento;

import java.util.Calendar;
import java.util.GregorianCalendar;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.manager.wsdl.interfaces.TabelasAuxiliares;

public class DataMovimentoAVencerRegistro implements IDataMovimentoAVencer {
	
	private AitRepository aitRepository;
	private TabelasAuxiliares tabelasAuxiliares;
	
	public DataMovimentoAVencerRegistro() throws Exception {
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		tabelasAuxiliares = (TabelasAuxiliares) BeansFactory.get(TabelasAuxiliares.class);
	}

	@Override
	public GregorianCalendar calcularVencimento(int cdAit) throws Exception {
		Ait ait = aitRepository.get(cdAit);
		int prazo = tabelasAuxiliares.getPrazoMovimento(TipoStatusEnum.REGISTRO_INFRACAO.getKey());	
		GregorianCalendar dataVencimento = ait.getDtInfracao();
		dataVencimento.set(Calendar.AM_PM, 0);
		dataVencimento.set(Calendar.HOUR, 0);
		dataVencimento.set(Calendar.MINUTE, 0);
		dataVencimento.set(Calendar.SECOND, 0);
		dataVencimento.set(Calendar.MILLISECOND, 0);
		dataVencimento.add(Calendar.DATE, prazo);
		return dataVencimento;
	}
	
}
