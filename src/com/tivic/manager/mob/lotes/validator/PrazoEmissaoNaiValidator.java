package com.tivic.manager.mob.lotes.validator;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDataPandemiaValidator;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lotes.service.impressao.exceptions.LoteNotificacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.util.date.DateUtil;

public class PrazoEmissaoNaiValidator implements IValidator<Ait> {
	
	private InconsistenciaRepository inconsistenciaRepository;
	private IParametroRepository parametroRepository;
	
	public PrazoEmissaoNaiValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		AitDataPandemiaValidator aitDataPandemiaValidator = new AitDataPandemiaValidator();
		int prazoEnvioNAI = -(this.parametroRepository.getValorOfParametroAsInt("MOB_PRAZOS_ENVIO_NAI"));
		GregorianCalendar dtPrazo = DateUtil.getDataAtual();
		GregorianCalendar dtInfracao = object.getDtInfracao();
		dtPrazo.set(Calendar.HOUR, 0);
		dtPrazo.set(Calendar.MINUTE, 0);
		dtPrazo.set(Calendar.SECOND, 0);
		dtPrazo.add(Calendar.DATE, prazoEnvioNAI);	
		if (dtInfracao.before(dtPrazo) && !aitDataPandemiaValidator.validPeriodoPandemia(object.getDtInfracao())){
			int tpForaPrazoNai = this.parametroRepository.getValorOfParametroAsInt("MOB_INCONSISTENCIA_NAI_INTEMPESTIVO");
			Inconsistencia inconsistencia = inconsistenciaRepository.get(tpForaPrazoNai);
			throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
		}
	}

}
