package com.tivic.manager.mob.lote.impressao.viaunica.nai;

import com.tivic.manager.mob.lote.impressao.GerarDocumentoSegundaViaFactory;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacaoHandler;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaNAI  extends TipoImpressaoNotificacaoHandler {

	@Override
	public byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		Boolean printPortal = false;
		if (tipoImpressaoNotificacao.getContemMovimento()
				&& tipoImpressaoNotificacao.getMovimentoEnviado() 
				&& tipoImpressaoNotificacao.getRegistradoEmLote()) {
			return new GerarDocumentoSegundaViaFactory()
					.getStrategy(TipoLoteNotificacaoEnum.LOTE_NAI_VIA_UNICA.getKey())
					.gerarDocumentos(tipoImpressaoNotificacao.getCdAit(), printPortal, customConnection);
		} 
		else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}
	}

}
