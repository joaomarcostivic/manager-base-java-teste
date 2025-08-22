package com.tivic.manager.mob.lote.impressao.validator;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoException;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class SituacaoAitValidator implements Validator<Ait> {
	
	private InconsistenciaRepository inconsistenciaRepository;
	
	public SituacaoAitValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		int tpAitNaoConfirmada = ParametroServices.getValorOfParametroAsInteger("MOB_INCONSISTENCIA_SITUACAO_AIT_NAO_CONFIRMADA", 0);
		Inconsistencia inconsistencia = inconsistenciaRepository.get(tpAitNaoConfirmada);
		if (object.getStAit() != AitServices.ST_CONFIRMADO){
			throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
		}
	}

}
