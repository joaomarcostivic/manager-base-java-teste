package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaNaiLote extends TipoImpressaoNotificacaoHandler {

	private ILoteImpressaoService loteImpressaoService;
	private AitRepository aitRepository;
	
	public GeraSegundaViaNaiLote() throws Exception {
		this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (tipoImpressaoNotificacao.getContemMovimento() && tipoImpressaoNotificacao.getMovimentoEnviado() && !(tipoImpressaoNotificacao.getRegistradoEmLote())) {
			Ait ait = aitRepository.get(tipoImpressaoNotificacao.getCdAit());
			List<Ait> aitList = Arrays.asList(ait);
			LoteImpressao loteNotificacaoNaiViaUnica = this.loteImpressaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), TipoLoteImpressaoEnum.LOTE_NAI.getKey());
			loteNotificacaoNaiViaUnica.setLgGeracaoViaUnica(true);
			this.loteImpressaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNaiViaUnica, tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			return this.loteImpressaoService.imprimirLoteNotificacao(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), customConnection);
		}
		else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}
	}
	
}	
