package com.tivic.manager.mob.lote.impressao;

import java.util.GregorianCalendar;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;

public class BuilderMovimentoFimPrazoDefesa {
	private AitMovimento aitMovimento;
	
	public BuilderMovimentoFimPrazoDefesa(Ait ait, int cdUsuario) {
		aitMovimento = new AitMovimento();
		aitMovimento.setCdAit(ait.getCdAit());
		aitMovimento.setDtMovimento(new GregorianCalendar());
		aitMovimento.setTpStatus(AitMovimentoServices.FIM_PRAZO_DEFESA);
		aitMovimento.setNrProcesso(Integer.toString(ait.getNrNotificacaoNip()));
		aitMovimento.setCdUsuario(cdUsuario);
	}
	
	public AitMovimento build() {
		return this.aitMovimento;
	}
}
