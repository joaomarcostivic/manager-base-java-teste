package com.tivic.manager.mob.lotes.factory.impressao;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.service.impressao.consulta.IListAitsCandidatasNotificacao;
import com.tivic.manager.mob.lotes.service.impressao.consulta.ListAitsCadidatasFimPrazoDefesaService;
import com.tivic.manager.mob.lotes.service.impressao.consulta.ListAitsCandidatosNicNipViaUnicaService;
import com.tivic.manager.mob.lotes.service.impressao.consulta.ListAitsCandidatosNipService;
import com.tivic.manager.mob.lotes.service.impressao.consulta.ListAitsCandidatosNipViaUnicaService;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ListAitsCandidatosNipFactory {
	private static final Map<Integer, Class<? extends IListAitsCandidatasNotificacao>> map = new HashMap<>();

	static {
		map.put(TipoLoteImpressaoEnum.LOTE_NIP_VIA_UNICA.getKey(), ListAitsCandidatosNipViaUnicaService.class);
		map.put(TipoLoteImpressaoEnum.LOTE_NIP.getKey(), ListAitsCandidatosNipService.class);
		map.put(TipoLoteImpressaoEnum.LOTE_NIC_NIP_VIA_UNICA.getKey(), ListAitsCandidatosNicNipViaUnicaService.class);
		map.put(TipoLoteImpressaoEnum.LOTE_FIM_PRAZO_DEFESA.getKey(), ListAitsCadidatasFimPrazoDefesaService.class);
	}

	public IListAitsCandidatasNotificacao getEstrategiaListAits(int tipoLoteNotificacao, SearchCriterios searchCriterios, CustomConnection customConnection) throws ValidacaoException, Exception {
		Class<? extends IListAitsCandidatasNotificacao> classe = map.get(tipoLoteNotificacao);
		if (classe == null) {
			throw new ValidacaoException("Tipo de lote não encontrado.");
		}
		try {
            Constructor<? extends IListAitsCandidatasNotificacao> constructor = classe.getConstructor(SearchCriterios.class, CustomConnection.class);
            return constructor.newInstance(searchCriterios, customConnection);
        } catch (NoSuchMethodException e) {
            throw new Exception("Erro ao instanciar a estratégia.", e);
        }
	}
}
