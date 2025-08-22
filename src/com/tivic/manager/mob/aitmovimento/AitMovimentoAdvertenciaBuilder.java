package com.tivic.manager.mob.aitmovimento;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;

public class AitMovimentoAdvertenciaBuilder {
	private AitMovimento aitMovimentoAdvertencia;

	public AitMovimentoAdvertenciaBuilder(int cdAit, int cdUsuario) throws Exception {
		aitMovimentoAdvertencia = new AitMovimento();
		aitMovimentoAdvertencia.setCdAit(cdAit);
		aitMovimentoAdvertencia.setDtMovimento(new GregorianCalendar());
		aitMovimentoAdvertencia.setTpStatus(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
		aitMovimentoAdvertencia.setCdUsuario(cdUsuario);
	}

	public AitMovimento build() {
		return aitMovimentoAdvertencia;
	}
}
