package com.tivic.manager.mob.lote.impressao.viaunica.nai;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacaoHandler;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaNAILote extends TipoImpressaoNotificacaoHandler {
	
	private ILoteNotificacaoService loteNotificacaoService;
	private AitRepository aitRepository;
	
	public GeraSegundaViaNAILote() throws Exception {
		this.loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (tipoImpressaoNotificacao.getContemMovimento() && tipoImpressaoNotificacao.getMovimentoEnviado() && !(tipoImpressaoNotificacao.getRegistradoEmLote())) {
			Ait ait = aitRepository.get(tipoImpressaoNotificacao.getCdAit());
			List<Ait> aitList = Arrays.asList(ait);
			LoteImpressao loteNotificacaoNaiViaUnica = this.loteNotificacaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), TipoLoteNotificacaoEnum.LOTE_NAI.getKey());
			this.loteNotificacaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			return this.loteNotificacaoService.imprimirLoteNotificacao(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), customConnection);
		}
		else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}
	}

}
