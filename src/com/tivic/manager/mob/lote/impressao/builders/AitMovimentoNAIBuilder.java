package com.tivic.manager.mob.lote.impressao.builders;

import java.util.GregorianCalendar;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;

public class AitMovimentoNAIBuilder {
	
	private AitMovimento aitMovimento;
	
	public AitMovimentoNAIBuilder(int cdAit, int cdUsuario) {
		this.aitMovimento = new AitMovimento();
		this.aitMovimento.setCdAit(cdAit);
		this.aitMovimento.setTpStatus(TipoStatusEnum.NAI_ENVIADO.getKey());
		this.aitMovimento.setDtMovimento(new GregorianCalendar());
		this.aitMovimento.setCdUsuario(cdUsuario);
	}
	
	public AitMovimento build() {
		return this.aitMovimento;
	}
	
}
