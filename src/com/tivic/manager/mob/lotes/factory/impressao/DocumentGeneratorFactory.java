package com.tivic.manager.mob.lotes.factory.impressao;

import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.impressao.strategy.documento.DocumentGeneratorNai;
import com.tivic.manager.mob.lotes.impressao.strategy.documento.DocumentGeneratorNip;
import com.tivic.manager.mob.lotes.impressao.strategy.documento.IDocumentGeneratorStrategy;
import com.tivic.manager.mob.lotes.utils.ParametroWrapper;
import com.tivic.sol.connection.CustomConnection;

public class DocumentGeneratorFactory {
    public static IDocumentGeneratorStrategy getStrategy(int tpDocumento, CustomConnection customConnection) throws Exception {
	    if (tpDocumento == TipoLoteImpressaoEnum.LOTE_NAI.getKey() || 
	        tpDocumento == TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey() ||
	        tpDocumento == TipoLoteImpressaoEnum.LOTE_NIC_NAI.getKey()) {
	        return getNAIStrategy(customConnection);
	    } else if (tpDocumento == TipoLoteImpressaoEnum.LOTE_NIP.getKey() || 
	               tpDocumento == TipoLoteImpressaoEnum.LOTE_NIP_VIA_UNICA.getKey() ||
	               tpDocumento == TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey()) {
	        return getNIPStrategy(customConnection);
	    } else {
	        throw new Exception("Tipo de documento não suportado");
	    }
    }

    private static IDocumentGeneratorStrategy getNAIStrategy(CustomConnection customConnection) throws Exception {
        return new DocumentGeneratorNai(TipoRemessaCorreiosEnum.fromCode(ParametroWrapper.asInt("MOB_TIPO_ENVIO_CORREIOS_NAI", customConnection)));
    }

    private static IDocumentGeneratorStrategy getNIPStrategy(CustomConnection customConnection) throws Exception {
        return new DocumentGeneratorNip(TipoRemessaCorreiosEnum.fromCode(ParametroWrapper.asInt("MOB_TIPO_ENVIO_CORREIOS_NIP", customConnection)));
    }

}
