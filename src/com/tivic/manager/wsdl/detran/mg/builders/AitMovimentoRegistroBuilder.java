package com.tivic.manager.wsdl.detran.mg.builders;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.util.Util;

public class AitMovimentoRegistroBuilder {

	private AitMovimento aitMovimento;
	
	public AitMovimentoRegistroBuilder(Ait ait) {
		aitMovimento = new AitMovimento();
		aitMovimento.setCdAit(ait.getCdAit());
		aitMovimento.setTpStatus(TipoStatusEnum.REGISTRO_INFRACAO.getKey());
		aitMovimento.setNrMovimento(1);
		aitMovimento.setDtMovimento(Util.getDataAtual());
	}
	
	public AitMovimento build() {
		return this.aitMovimento;
	}
}
