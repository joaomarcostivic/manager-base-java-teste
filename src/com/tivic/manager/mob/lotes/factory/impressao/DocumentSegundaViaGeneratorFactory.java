package com.tivic.manager.mob.lotes.factory.impressao;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.impressao.strategy.documento.IGeraSegundaViaImpressao;
import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.DocumentGeneratorSegundaViaNai;
import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.DocumentGeneratorSegundaViaNip;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class DocumentSegundaViaGeneratorFactory {
	
	private static final Map<Integer, Class<? extends IGeraSegundaViaImpressao>> map = new HashMap<>();

    static {
        map.put(TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey(), DocumentGeneratorSegundaViaNai.class);
        map.put(TipoLoteImpressaoEnum.LOTE_NIP_VIA_UNICA.getKey(), DocumentGeneratorSegundaViaNip.class);
	    map.put(TipoLoteImpressaoEnum.LOTE_NIC_NAI_VIA_UNICA.getKey(), DocumentGeneratorSegundaViaNai.class);
	    map.put(TipoLoteImpressaoEnum.LOTE_NIC_NIP_VIA_UNICA.getKey(), DocumentGeneratorSegundaViaNip.class);
    }

    public IGeraSegundaViaImpressao getStrategy(int tipoDocumento) throws ValidacaoException, Exception {
        Class<? extends IGeraSegundaViaImpressao> classe = map.get(tipoDocumento);
        if (classe == null) {
            throw new ValidacaoException("Tipo de documento não encontrado.");
        }
        return classe.newInstance();
    }

}
