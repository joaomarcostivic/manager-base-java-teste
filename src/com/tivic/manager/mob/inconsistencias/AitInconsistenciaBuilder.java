package com.tivic.manager.mob.inconsistencias;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.sol.cdi.BeansFactory;

public class AitInconsistenciaBuilder {
	
	private InconsistenciaRepository inconsistenciaRepository;
	
	public AitInconsistenciaBuilder() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
	}

	public AitInconsistencia build(Ait ait, int tpInconsistencia, int tpStatusPretendido) throws Exception {
		AitInconsistencia aitInconsistencia = new AitInconsistencia();
		aitInconsistencia.setCdAit(ait.getCdAit());
		aitInconsistencia.setCdInconsistencia(inconsistenciaRepository.get(tpInconsistencia).getCdInconsistencia());
		aitInconsistencia.setCdMovimentoAtual(ait.getCdMovimentoAtual());
		aitInconsistencia.setDtInclusaoInconsistencia(new GregorianCalendar());
		aitInconsistencia.setStInconsistencia(TipoSituacaoInconsistencia.PENDENTE.getKey());
		aitInconsistencia.setTpStatusAtual(ait.getTpStatus());
		aitInconsistencia.setTpStatusPretendido(tpStatusPretendido);
		return aitInconsistencia;
	}

}
