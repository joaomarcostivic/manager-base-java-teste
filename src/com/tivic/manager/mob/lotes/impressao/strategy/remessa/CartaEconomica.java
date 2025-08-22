package com.tivic.manager.mob.lotes.impressao.strategy.remessa;

import com.tivic.manager.mob.lotes.factory.impressao.TipoRemessaDocumentFactory;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.sol.connection.CustomConnection;

public class CartaEconomica implements TipoRemessaStrategy {
	
	@Override
	public TipoRemessaDocumentFactory getDadosDocumento(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
	    return new TipoRemessaDocumentFactory(loteImpressao, customConnection)
	            .setNotificacoes()
	            .setARParameters(customConnection)
	            .setEtiquetas(customConnection)
	            .setArquivoPostagem(customConnection);
	}
}