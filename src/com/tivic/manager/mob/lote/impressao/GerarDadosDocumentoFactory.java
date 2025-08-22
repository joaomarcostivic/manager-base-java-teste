package com.tivic.manager.mob.lote.impressao;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.lote.impressao.remessacorreios.CartaEconomica;
import com.tivic.manager.mob.lote.impressao.remessacorreios.CartaRegistrada;
import com.tivic.manager.mob.lote.impressao.remessacorreios.CartaSimples;
import com.tivic.manager.mob.lote.impressao.remessacorreios.IGerarDadosDocumento;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GerarDadosDocumentoFactory {
	private static final Map<Integer, Class<? extends IGerarDadosDocumento>> map = new HashMap<>();

	static {
		map.put(TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey(), CartaSimples.class);
		map.put(TipoRemessaCorreiosEnum.CARTA_REGISTRADA.getKey(), CartaRegistrada.class);
		map.put(TipoRemessaCorreiosEnum.REMESSA_ECONOMICA.getKey(), CartaEconomica.class);
	}

	public IGerarDadosDocumento getStrategy(int tpRemessa) throws ValidacaoException, Exception {
		int tpRemessaCorreios = (tpRemessa == TipoLoteNotificacaoEnum.LOTE_NAI.getKey() || tpRemessa == TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey()) 
               ? Integer.parseInt(ParametroServices.getValorOfParametro("MOB_TIPO_ENVIO_CORREIOS_NAI"))
               : Integer.parseInt(ParametroServices.getValorOfParametro("MOB_TIPO_ENVIO_CORREIOS_NIP"));
		Class<? extends IGerarDadosDocumento> classe = map.get(tpRemessaCorreios);
		if (classe == null) {
			throw new ValidacaoException("Tipo de envio não encontrado.");
		}

		return classe.newInstance();
	}
}
