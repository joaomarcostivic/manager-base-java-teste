package com.tivic.manager.mob.lote.impressao.validator;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoException;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CpfCnpjProprietarioValidator implements Validator<Ait> {

	private InconsistenciaRepository inconsistenciaRepository;

	public CpfCnpjProprietarioValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		if (object.getNrCpfCnpjProprietario() == null || object.getNrCpfCnpjProprietario().trim().equals("")) {
			int tpSemCpfCnpjProprietario = ParametroServices.getValorOfParametroAsInteger("MOB_INCONSISTENCIA_SEM_CPF_CNPJ_PROPRIETARIO", 0);
			Inconsistencia inconsistencia = inconsistenciaRepository.get(tpSemCpfCnpjProprietario);
			throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
		}
	}

}
