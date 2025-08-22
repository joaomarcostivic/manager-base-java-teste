package com.tivic.manager.wsdl.detran.mg.builders;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.util.Util;

public class AitMovimentoCancelarAutoInfracaoBuilder {

	private AitMovimento aitMovimento;
	
	public AitMovimentoCancelarAutoInfracaoBuilder(Ait ait) {
		aitMovimento = new AitMovimento();
		aitMovimento.setCdAit(ait.getCdAit());
		aitMovimento.setTpStatus(TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey());
		aitMovimento.setNrMovimento(1);
		aitMovimento.setLgEnviadoDetran(0);
		aitMovimento.setDtMovimento(Util.getDataAtual());
		aitMovimento.setCdOcorrencia(1);
	}
	
	public AitMovimento build() {
		return this.aitMovimento;
	}
}
