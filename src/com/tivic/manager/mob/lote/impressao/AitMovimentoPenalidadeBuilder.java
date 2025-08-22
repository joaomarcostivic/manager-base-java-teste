package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.util.Util;

public class AitMovimentoPenalidadeBuilder {
	private AitMovimento aitMovimento;
	
	public AitMovimentoPenalidadeBuilder(int cdAit, int cdUsuario) {
		aitMovimento = new AitMovimento();
		aitMovimento.setCdUsuario(cdUsuario);
		aitMovimento.setCdAit(cdAit);
		aitMovimento.setDtMovimento(Util.getDataAtual());
		aitMovimento.setTpStatus(TipoStatusEnum.NIP_ENVIADA.getKey());
	}
	
	public AitMovimento build() {
		return aitMovimento;
	}
}
