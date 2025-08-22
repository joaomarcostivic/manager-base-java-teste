package com.tivic.manager.mob.lotes.builders.impressao;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.sol.util.date.DateUtil;

public class MovimentoFimPrazoDefesaBuilder {
private AitMovimento aitMovimento;
	
	public MovimentoFimPrazoDefesaBuilder(Ait ait, int cdUsuario) {
		aitMovimento = new AitMovimento();
		aitMovimento.setCdAit(ait.getCdAit());
		aitMovimento.setDtMovimento(DateUtil.getDataAtual());
		aitMovimento.setTpStatus(TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
		aitMovimento.setNrProcesso(Integer.toString(ait.getNrNotificacaoNip()));
		aitMovimento.setCdUsuario(cdUsuario);
	}
	
	public AitMovimento build() {
		return this.aitMovimento;
	}
}
