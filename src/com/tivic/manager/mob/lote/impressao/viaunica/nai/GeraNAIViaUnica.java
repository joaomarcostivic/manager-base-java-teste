package com.tivic.manager.mob.lote.impressao.viaunica.nai;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.lote.impressao.GerarMovimentoNotificacaoFactory;
import com.tivic.manager.mob.lote.impressao.ListAitsCandidatosNaiFactory;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacaoHandler;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;

public class GeraNAIViaUnica extends TipoImpressaoNotificacaoHandler {
	
	private ILoteNotificacaoService loteNotificacaoService;
	
	public GeraNAIViaUnica() throws Exception {
		this.loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
	}

	@Override
	public byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (!(tipoImpressaoNotificacao.getContemMovimento() || tipoImpressaoNotificacao.getMovimentoEnviado() || tipoImpressaoNotificacao.getRegistradoEmLote())) {
			List<Ait> aitList = new ListAitsCandidatosNaiFactory()
					.getEstrategiaListAits(TipoLoteNotificacaoEnum.LOTE_NAI_VIA_UNICA.getKey())
					.build(searchNotificacaoViaUnica(tipoImpressaoNotificacao.getCdAit()).getCriterios(), customConnection.getConnection());
			new GerarMovimentoNotificacaoFactory()
				.strategy(TipoLoteNotificacaoEnum.LOTE_NAI_VIA_UNICA.getKey())
				.gerarMovimentos(aitList, tipoImpressaoNotificacao.getCdUsuario());
			LoteImpressao loteNotificacaoNaiViaUnica = this.loteNotificacaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), TipoLoteNotificacaoEnum.LOTE_NAI.getKey());
			this.loteNotificacaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			return this.loteNotificacaoService.imprimirLoteNotificacao(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), customConnection);
		} else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}
	}
	
	private SearchCriterios searchNotificacaoViaUnica(int cdAit) {
		SearchCriterios search = new SearchCriterios();
		search.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return search;
	}

}
