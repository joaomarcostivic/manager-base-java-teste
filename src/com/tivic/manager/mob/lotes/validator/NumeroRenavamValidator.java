package com.tivic.manager.mob.lotes.validator;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lotes.service.impressao.exceptions.LoteNotificacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class NumeroRenavamValidator implements IValidator<Ait> {
	
	private InconsistenciaRepository inconsistenciaRepository;
	private IParametroRepository parametroRepository;

	public NumeroRenavamValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		if (object.getNrRenavan() == null || object.getNrRenavan().trim().equals("") || object.getNrRenavan().trim().equals("00000000000") ){
			int tpSemNumeroRenavam = parametroRepository.getValorOfParametroAsInt("MOB_INCONSISTENCIA_SEM_NR_RENAVAN");
			Inconsistencia inconsistencia = inconsistenciaRepository.get(tpSemNumeroRenavam);
			throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
		}
	}
}
