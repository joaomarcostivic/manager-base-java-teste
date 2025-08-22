package com.tivic.manager.mob.lote.impressao.viaunica;

import com.tivic.sol.connection.CustomConnection;

public abstract class TipoImpressaoNotificacaoHandler {

	protected TipoImpressaoNotificacaoHandler nextGenerator;
	
	public void setNextGenerator(TipoImpressaoNotificacaoHandler nextGenerator) {
		this.nextGenerator = nextGenerator;
	}
	
	public abstract byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception;
	
}
