package com.tivic.manager.mob.lote.impressao;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GerarLoteImpressaoFactory {

	private static final Map<Integer, Class<? extends IGerarLoteImpressao>> map = new HashMap<>();

	static {
		map.put(TipoLoteNotificacaoEnum.LOTE_NAI.getKey(), GerarLoteImpressaoNAI.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIP.getKey(), GerarLoteImpressaoNip.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey(), GeraLoteImpressaoNicNai.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey(), GeraLoteImpressaoNicNip.class);
	}

	public IGerarLoteImpressao getStrategy(int tipoDocumento) throws ValidacaoException, Exception {
		Class<? extends IGerarLoteImpressao> classe = map.get(tipoDocumento);
		if (classe == null) {
			throw new ValidacaoException("Tipo de lote não encontrado.");
		}

		return classe.newInstance();
	}
}
