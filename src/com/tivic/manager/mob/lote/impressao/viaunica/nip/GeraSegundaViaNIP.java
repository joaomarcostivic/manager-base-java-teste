package com.tivic.manager.mob.lote.impressao.viaunica.nip;

import com.tivic.manager.mob.lote.impressao.GerarDocumentoSegundaViaFactory;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaNIP extends TipoImpressaoNotificacaoNPHandler {
	Boolean printPortal = false;
	@Override
	public NipImpressaoDTO gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (!tipoImpressaoNotificacao.getMovimentoAdvertencia() && tipoImpressaoNotificacao.getContemMovimento() && tipoImpressaoNotificacao.getMovimentoEnviado() && tipoImpressaoNotificacao.getRegistradoEmLote()) {
			byte[] arquivo = new GerarDocumentoSegundaViaFactory()
					.getStrategy(TipoLoteNotificacaoEnum.LOTE_NIP_VIA_UNICA.getKey())
					.gerarDocumentos(tipoImpressaoNotificacao.getCdAit(), printPortal, customConnection);
			NipImpressaoDTO nipImpressaoDTO = new NipImpressaoDTOBuilder()
					.addCdAit(tipoImpressaoNotificacao.getCdAit())
					.addArquivo(arquivo)
					.build();
			return nipImpressaoDTO;
		} 
		else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}
	}

}
