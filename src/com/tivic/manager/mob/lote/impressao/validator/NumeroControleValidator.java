package com.tivic.manager.mob.lote.impressao.validator;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoException;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class NumeroControleValidator implements Validator<Ait> {
	
	private InconsistenciaRepository inconsistenciaRepository;
	
	public NumeroControleValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		if (object.getNrControle() == null || object.getNrControle().equals("0") || object.getNrControle().trim().equals("")){
			int tpSemNrControle = ParametroServices.getValorOfParametroAsInteger("MOB_INCONSISTENCIA_SEM_NUMERO_CONTROLE", 0);
			Inconsistencia inconsistencia = inconsistenciaRepository.get(tpSemNrControle);
			throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
		}
	}

}
