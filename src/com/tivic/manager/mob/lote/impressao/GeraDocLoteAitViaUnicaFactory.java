package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GeraDocLoteAitViaUnicaFactory {
	public IGerarDocumentosLoteImpressao getStrategy(LoteImpressao loteImpressao) throws Exception {
		if (loteImpressao.getTpDocumento() == TipoLoteNotificacaoEnum.LOTE_NAI.getKey()) {
			return new GeraDocImpressaoNaiViaUnica();
		} 
		else if(loteImpressao.getTpDocumento() == TipoLoteNotificacaoEnum.LOTE_NIP.getKey() || loteImpressao.getTpDocumento() == TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey()) {
			return new GeraDocImpressaoNipViaUnica();
		}
		else {
			throw new ValidacaoException("Tipo de lote não encontrado.");
		}
	}
}
