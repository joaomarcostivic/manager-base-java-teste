package com.tivic.manager.mob.AitInconsistenciaBuilder;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.InconsistenciaDAO;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistenciaServices;

public class AitInconsistenciaBuilder {
	public AitInconsistencia build(Ait ait, int tpInconsistencia, int tpStatusPretendido) {
		AitInconsistencia aitInconsistencia = new AitInconsistencia();
		aitInconsistencia.setCdAit(ait.getCdAit());
		aitInconsistencia.setCdInconsistencia(InconsistenciaDAO.get(tpInconsistencia).getCdInconsistencia());
		aitInconsistencia.setCdMovimentoAtual(ait.getCdMovimentoAtual());
		aitInconsistencia.setDtInclusaoInconsistencia(new GregorianCalendar());
		aitInconsistencia.setStInconsistencia(AitInconsistenciaServices.PENDETE);
		aitInconsistencia.setTpStatusAtual(ait.getTpStatus());
		aitInconsistencia.setTpStatusPretendido(tpStatusPretendido);
		return aitInconsistencia;
	}
}
