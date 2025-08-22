package com.tivic.manager.mob.lotes.validator;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lotes.service.impressao.exceptions.LoteNotificacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class SituacaoAitValidator implements IValidator<Ait> {
	
	private InconsistenciaRepository inconsistenciaRepository;
	private IParametroRepository parametroRepository;

	public SituacaoAitValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		int tpAitNaoConfirmada = parametroRepository.getValorOfParametroAsInt("MOB_INCONSISTENCIA_SITUACAO_AIT_NAO_CONFIRMADA");
		Inconsistencia inconsistencia = inconsistenciaRepository.get(tpAitNaoConfirmada);
		if (object.getStAit() != AitServices.ST_CONFIRMADO){
			throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
		}
	}
}
