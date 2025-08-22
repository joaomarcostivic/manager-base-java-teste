package com.tivic.manager.mob.lotes.factory.impressao;

import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.impressao.strategy.documento.DocumentGeneratorNai;
import com.tivic.manager.mob.lotes.impressao.strategy.documento.DocumentGeneratorNip;
import com.tivic.manager.mob.lotes.impressao.strategy.documento.IDocumentGeneratorStrategy;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;

public class DocumentViaUnicaGeneratorFactory {
	
	public static IDocumentGeneratorStrategy createGenerator(LoteImpressao loteImpressao) throws Exception {

	    TipoLoteImpressaoEnum tipoImpressao = TipoLoteImpressaoEnum.fromCode(loteImpressao.getTpImpressao());
	    TipoRemessaCorreiosEnum tipoRemessa = TipoRemessaCorreiosEnum.CARTA_SIMPLES;
	    if (tipoImpressao == TipoLoteImpressaoEnum.LOTE_NAI || tipoImpressao == TipoLoteImpressaoEnum.LOTE_NIC_NAI) {
	        return new DocumentGeneratorNai(tipoRemessa);
	    }
	    if (tipoImpressao == TipoLoteImpressaoEnum.LOTE_NIP || tipoImpressao == TipoLoteImpressaoEnum.LOTE_NIC_NIP) {
	        return new DocumentGeneratorNip(tipoRemessa);
	    }

	    throw new UnsupportedOperationException("Tipo de documento de impressão não suportado: " + tipoImpressao);
	}

}
