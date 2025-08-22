package com.tivic.manager.mob.lote.impressao;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.lote.impressao.viaunica.nic.GeraDocumentoSegundaViaNicNai;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.GeraDocumentoSegundaViaNicNip;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GerarDocumentoSegundaViaFactory {
    private static final Map<Integer, Class<? extends IGerarSegundaViaImpressao>> map = new HashMap<>();

    static {
        map.put(TipoLoteNotificacaoEnum.LOTE_NAI_VIA_UNICA.getKey(), GerarDocumentoSegundaViaNAI.class);
        map.put(TipoLoteNotificacaoEnum.LOTE_NIP_VIA_UNICA.getKey(), GerarDocumentoSegundaViaNIP.class);
        map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NAI_VIA_UNICA.getKey(), GeraDocumentoSegundaViaNicNai.class);
        map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NIP_VIA_UNICA.getKey(), GeraDocumentoSegundaViaNicNip.class);
    }

    public IGerarSegundaViaImpressao getStrategy(int tipoDocumento) throws ValidacaoException, Exception {
        Class<? extends IGerarSegundaViaImpressao> classe = map.get(tipoDocumento);
        if (classe == null) {
            throw new ValidacaoException("Tipo de documento não encontrado.");
        }
       
        return classe.newInstance();
    }
}
