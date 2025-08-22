package com.tivic.manager.ptc.protocolos.julgamento;

import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GerarDocumentosLoteJulgamentoFactory {
	public IGerarDocumentosLoteJulgamento getStrategy(LoteImpressao loteImpressao) throws Exception {
		if (loteImpressao.getTpImpressao() == TipoLoteJulgamentoEnum.LOTE_DEFESA_DEFERIDA.getKey()) {
			return new GerarDocumentoJulgamentoDefesaDeferida();
		} 
		else if (loteImpressao.getTpImpressao() == TipoLoteJulgamentoEnum.LOTE_JARI_COM_PROVIMENTO.getKey()) {
			return new GerarDocumentoJulgamentoJariDeferida();
		}
		else if (loteImpressao.getTpImpressao() == TipoLoteJulgamentoEnum.LOTE_JARI_SEM_PROVIMENTO.getKey()) {
			return new GerarDocumentoJulgamentoJariIndeferida();
		}
		else {
			throw new ValidacaoException("Tipo de documento não encontrado.");
		}
	}
}
