package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class BuscarAitsParaLoteImpressaoFactory{
	public IBuscarAitsParaLoteImpressao getStrategy(int tpDocumento) throws Exception {
		if (tpDocumento == TipoLoteNotificacaoEnum.LOTE_NAI.getKey() || tpDocumento == TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey()) {
			return new BuscarAitsParaLoteImpressaoNai();
		} 
		else if(tpDocumento == TipoLoteNotificacaoEnum.LOTE_NIP.getKey() || tpDocumento == TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey()) {
			return new BuscarAitsParaLoteImpressaoNip();
		}
		else {
			throw new ValidacaoException("Tipo de lote não encontrado.");
		}
	}
}
