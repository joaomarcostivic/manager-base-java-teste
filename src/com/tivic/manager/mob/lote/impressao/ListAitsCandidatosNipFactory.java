package com.tivic.manager.mob.lote.impressao;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.lote.impressao.ListAitsCandidatosNipBuilder.ListAitsCandidatosNipBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ListAitsCandidatosNipFactory {
	
	private static final Map<Integer, Class<? extends IListAitsCandidatasNotificacao>> map = new HashMap<>();

	static {
		map.put(TipoLoteNotificacaoEnum.LOTE_NIP_VIA_UNICA.getKey(), ListAitsCandidatosNipViaUnicaBuilder.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIP.getKey(), ListAitsCandidatosNipBuilder.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NIP_VIA_UNICA.getKey(), ListAitsCandidatosNicNipViaUnicaBuilder.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_FIM_PRAZO_DEFESA.getKey(), ListAitsCadidatasFimPrazoDefesaBuilder.class);
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
