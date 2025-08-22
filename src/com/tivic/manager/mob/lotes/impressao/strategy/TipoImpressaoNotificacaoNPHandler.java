package com.tivic.manager.mob.lotes.impressao.strategy;

import com.tivic.manager.mob.lotes.dto.impressao.NipImpressaoDTO;
import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.TipoImpressaoNotificacao;
import com.tivic.sol.connection.CustomConnection;

public abstract class TipoImpressaoNotificacaoNPHandler {
	protected TipoImpressaoNotificacaoNPHandler nextGenerator;

	public void setNextGenerator(TipoImpressaoNotificacaoNPHandler nextGenerator) {
		this.nextGenerator = nextGenerator;
	}

	public abstract NipImpressaoDTO gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao,
			CustomConnection customConnection) throws Exception;
}
