package com.tivic.manager.mob.lotes.factory.impressao;

import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lotes.impressao.strategy.remessa.CartaEconomica;
import com.tivic.manager.mob.lotes.impressao.strategy.remessa.CartaRegistrada;
import com.tivic.manager.mob.lotes.impressao.strategy.remessa.CartaSimples;
import com.tivic.manager.mob.lotes.impressao.strategy.remessa.TipoRemessaStrategy;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class TipoRemessaFactory {
	
	public static TipoRemessaStrategy getStrategy(TipoRemessaCorreiosEnum tpRemessaCorreios) throws Exception {
	    switch (tpRemessaCorreios) {
	        case CARTA_SIMPLES:
	            return new CartaSimples();
	        case CARTA_REGISTRADA:
	            return new CartaRegistrada();
	        case REMESSA_ECONOMICA:
	            return new CartaEconomica();
	        default:
	            throw new ValidacaoException("Tipo de remessa não suportada.");
	    }
	}
}
