package com.tivic.manager.ptc.protocolos.julgamento;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GerarLoteJulgamentoFactory {
	private static final Map<Integer, Class<? extends IGerarLoteJulgamento>> map = new HashMap<>();

	 static {
	        map.put(TipoLoteJulgamentoEnum.LOTE_DEFESA_DEFERIDA.getKey(), GerarLoteJulgamento.class);
	        map.put(TipoLoteJulgamentoEnum.LOTE_JARI_COM_PROVIMENTO.getKey(), GerarLoteJulgamento.class);
		    map.put(TipoLoteJulgamentoEnum.LOTE_JARI_SEM_PROVIMENTO.getKey(), GerarLoteJulgamento.class);
	 }

    public IGerarLoteJulgamento getStrategy(int tpDocumento) throws ValidacaoException, Exception {
        Class<? extends IGerarLoteJulgamento> classe = map.get(tpDocumento);
        if (classe == null) {
            throw new ValidacaoException("Tipo de documento não encontrado.");
        }
        return classe.newInstance();
    }

}
