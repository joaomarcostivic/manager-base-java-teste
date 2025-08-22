package com.tivic.manager.mob.lote.impressao.validator;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDataPandemiaValidator;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoException;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class PrazoEmissaoNaiValidator implements Validator<Ait> {
	
	private InconsistenciaRepository inconsistenciaRepository;
	
	public PrazoEmissaoNaiValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		AitDataPandemiaValidator aitDataPandemiaValidator = new AitDataPandemiaValidator();
		int prazoEnvioNAI = -(ParametroServices.getValorOfParametroAsInteger("MOB_PRAZOS_ENVIO_NAI", 0));
		GregorianCalendar dtPrazo = new GregorianCalendar();
		GregorianCalendar dtInfracao = object.getDtInfracao();
		dtPrazo.set(Calendar.HOUR, 0);
		dtPrazo.set(Calendar.MINUTE, 0);
		dtPrazo.set(Calendar.SECOND, 0);
		dtPrazo.add(Calendar.DATE, prazoEnvioNAI);	
		if (dtInfracao.before(dtPrazo) && !aitDataPandemiaValidator.validPeriodoPandemia(object.getDtInfracao())){
			int tpForaPrazoNai = ParametroServices.getValorOfParametroAsInteger("MOB_INCONSISTENCIA_NAI_INTEMPESTIVO", 0);
			Inconsistencia inconsistencia = inconsistenciaRepository.get(tpForaPrazoNai);
			throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
		}
	}

}
