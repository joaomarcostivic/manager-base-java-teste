package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.mob.lote.impressao.ListAitsCandidatosLoteNaiBuilder.ListAitsCandidatosLoteNaiBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import java.util.HashMap;
import java.util.Map;

public class ListAitsCandidatosNaiFactory {

	private static final Map<Integer, Class<? extends IListAitsCandidatosNai>> map = new HashMap<>();

	static {
		map.put(TipoLoteNotificacaoEnum.LOTE_NAI_VIA_UNICA.getKey(), ListAitsCandidatosNaiViaUnicaBuilder.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NAI.getKey(), ListAitsCandidatosLoteNaiBuilder.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NAI_VIA_UNICA.getKey(), ListAitsCandidatosNicNaiViaUnicaBuilder.class);
	}

	public IListAitsCandidatosNai getEstrategiaListAits(int tipoLoteNotificacao) throws ValidacaoException, Exception {
		Class<? extends IListAitsCandidatosNai> classe = map.get(tipoLoteNotificacao);
		if (classe == null) {
			throw new ValidacaoException("Tipo de Lote não encontrado");
		}

		return classe.newInstance();
	}
}