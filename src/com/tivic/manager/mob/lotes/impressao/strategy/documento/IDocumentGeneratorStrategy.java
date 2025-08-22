package com.tivic.manager.mob.lotes.impressao.strategy.documento;

import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.sol.connection.CustomConnection;

public interface IDocumentGeneratorStrategy {
	public byte[] generate(LoteImpressao loteImpressao, String idLote, CustomConnection customConnection) throws Exception;
}
