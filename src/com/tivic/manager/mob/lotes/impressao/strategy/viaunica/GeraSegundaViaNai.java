package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.factory.impressao.DocumentSegundaViaGeneratorFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaNai extends TipoImpressaoNotificacaoHandler {
	
	@Override
	public byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		Boolean printPortal = false;
		if (tipoImpressaoNotificacao.getContemMovimento()
				&& tipoImpressaoNotificacao.getMovimentoEnviado() 
				&& tipoImpressaoNotificacao.getRegistradoEmLote()) {
			
			return new DocumentSegundaViaGeneratorFactory()
					.getStrategy(TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey())
					.gerarDocumentos(tipoImpressaoNotificacao.getCdAit(), printPortal, customConnection);
		} 
		else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}
	}

}
