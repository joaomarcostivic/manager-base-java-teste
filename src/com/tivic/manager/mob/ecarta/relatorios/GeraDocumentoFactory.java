package com.tivic.manager.mob.ecarta.relatorios;

import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GeraDocumentoFactory {
	
	public IECTGeraDocumento getStrategy(int tpDocumentoLote) throws Exception {
		if (tpDocumentoLote == TipoLoteNotificacaoEnum.LOTE_NAI.getKey()) {
			return new ECTGeraDocumentoNAI();
		} 
		else if (tpDocumentoLote == TipoLoteNotificacaoEnum.LOTE_NIP.getKey()) {
			return new ECTGeraDocumentoNIP();
		} else {
			throw new ValidacaoException("Tipo de documento não identificado.");
		}
	}
	
}
