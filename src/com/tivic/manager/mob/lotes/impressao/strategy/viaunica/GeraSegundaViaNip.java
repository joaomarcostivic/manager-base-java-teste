package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import com.tivic.manager.mob.lotes.builders.impressao.viaunica.NipImpressaoDTOBuilder;
import com.tivic.manager.mob.lotes.dto.impressao.NipImpressaoDTO;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.factory.impressao.DocumentSegundaViaGeneratorFactory;
import com.tivic.manager.mob.lotes.impressao.strategy.TipoImpressaoNotificacaoNPHandler;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaNip extends TipoImpressaoNotificacaoNPHandler {
	Boolean printPortal = false;
	
	@Override
	public NipImpressaoDTO gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (!tipoImpressaoNotificacao.getMovimentoAdvertencia() && tipoImpressaoNotificacao.getContemMovimento() && tipoImpressaoNotificacao.getMovimentoEnviado() && tipoImpressaoNotificacao.getRegistradoEmLote()) {
			byte[] arquivo = new DocumentSegundaViaGeneratorFactory()
					.getStrategy(TipoLoteImpressaoEnum.LOTE_NIP_VIA_UNICA.getKey())
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
