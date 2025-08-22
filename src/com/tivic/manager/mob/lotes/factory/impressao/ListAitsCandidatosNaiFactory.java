package com.tivic.manager.mob.lotes.factory.impressao;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.service.impressao.consulta.IListAitsCandidatosNai;
import com.tivic.manager.mob.lotes.service.impressao.consulta.ListAitsCandidatosLoteNaiService;
import com.tivic.manager.mob.lotes.service.impressao.consulta.ListAitsCandidatosNaiViaUnicaService;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.search.SearchCriterios;

public class ListAitsCandidatosNaiFactory {
	private static final Map<Integer, Class<? extends IListAitsCandidatosNai>> map = new HashMap<>();

	static {
		map.put(TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey(), ListAitsCandidatosNaiViaUnicaService.class);
		map.put(TipoLoteImpressaoEnum.LOTE_NAI.getKey(), ListAitsCandidatosLoteNaiService.class);
		map.put(TipoLoteImpressaoEnum.LOTE_NIC_NAI_VIA_UNICA.getKey(), ListAitsCandidatosNaiViaUnicaService.class);
	}

	public IListAitsCandidatosNai getEstrategiaListAits(int tipoLoteNotificacao, SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Class<? extends IListAitsCandidatosNai> classe = map.get(tipoLoteNotificacao);
		if (classe == null) {
			throw new ValidacaoException("Tipo de Lote não encontrado");
		}
		try {
            Constructor<? extends IListAitsCandidatosNai> constructor = classe.getConstructor(SearchCriterios.class);
            return constructor.newInstance(searchCriterios);
        } catch (NoSuchMethodException e) {
            throw new Exception("Erro ao instanciar a estratégia.", e);
        }

	}
}
