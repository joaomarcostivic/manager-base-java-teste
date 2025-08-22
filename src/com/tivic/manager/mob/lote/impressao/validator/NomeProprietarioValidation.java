package com.tivic.manager.mob.lote.impressao.validator;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoException;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class NomeProprietarioValidation implements Validator<Ait> {
	
private InconsistenciaRepository inconsistenciaRepository;
	
	public NomeProprietarioValidation() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		int tpSemNomeProprietario = ParametroServices.getValorOfParametroAsInteger("MOB_INCONSISTENCIA_SEM_NOME_PROPRIETARIO", 0);
		Inconsistencia inconsistencia = inconsistenciaRepository.get(tpSemNomeProprietario);
		if (object.getNmProprietario() == null || object.getNmProprietario().trim().equals("")){
			throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
		}
	}

}
