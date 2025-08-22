package com.tivic.manager.mob.lote.impressao;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GerarDocumentosLoteImpressaoFactory {
	private static final Map<Integer, Class<? extends IGerarDocumentosLoteImpressao>> map = new HashMap<>();

	static {
		map.put(TipoLoteNotificacaoEnum.LOTE_NAI.getKey(), GerarDocumentosImpressaoNai.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIP.getKey(), GerarDocumentosImpressaoNip.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey(), GerarDocumentosImpressaoNai.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey(), GerarDocumentosImpressaoNip.class);
	}

	public IGerarDocumentosLoteImpressao getStrategy(LoteImpressao loteImpressao) throws ValidacaoException, Exception {
		Class<? extends IGerarDocumentosLoteImpressao> classe = map.get(loteImpressao.getTpDocumento());
		if (classe == null) {
			throw new ValidacaoException("Tipo de lote não encontrado.");
		}

		return classe.newInstance();
	}
}
