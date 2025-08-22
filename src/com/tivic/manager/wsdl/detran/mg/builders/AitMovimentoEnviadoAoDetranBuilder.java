package com.tivic.manager.wsdl.detran.mg.builders;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.util.Util;

public class AitMovimentoEnviadoAoDetranBuilder {

	private AitMovimento aitMovimento;
	
	public AitMovimentoEnviadoAoDetranBuilder(Ait ait) {
		aitMovimento = new AitMovimento();
		aitMovimento.setCdAit(ait.getCdAit());
		aitMovimento.setTpStatus(TipoStatusEnum.ENVIADO_AO_DETRAN.getKey());
		aitMovimento.setNrMovimento(1);
		aitMovimento.setLgEnviadoDetran(1);
		aitMovimento.setDtMovimento(Util.getDataAtual());
		aitMovimento.setDtRegistroDetran(Util.getDataAtual());
	}
	
	public AitMovimento build() {
		return this.aitMovimento;
	}
}
