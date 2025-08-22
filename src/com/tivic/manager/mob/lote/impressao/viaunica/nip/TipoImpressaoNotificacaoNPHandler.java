package com.tivic.manager.mob.lote.impressao.viaunica.nip;

import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.sol.connection.CustomConnection;

public abstract class TipoImpressaoNotificacaoNPHandler {
	protected TipoImpressaoNotificacaoNPHandler nextGenerator;

	public void setNextGenerator(TipoImpressaoNotificacaoNPHandler nextGenerator) {
		this.nextGenerator = nextGenerator;
	}

	public abstract NipImpressaoDTO gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao,
			CustomConnection customConnection) throws Exception;
}
