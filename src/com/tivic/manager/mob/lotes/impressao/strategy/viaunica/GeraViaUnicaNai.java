package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.factory.impressao.GeraMovimentoNotificacaoFactory;
import com.tivic.manager.mob.lotes.factory.impressao.ListAitsCandidatosNaiFactory;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraViaUnicaNai extends TipoImpressaoNotificacaoHandler {

	private ILoteImpressaoService loteImpressaoService;
	
	public GeraViaUnicaNai() throws Exception {
		this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
	}

	@Override
	public byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (!(tipoImpressaoNotificacao.getContemMovimento() || tipoImpressaoNotificacao.getMovimentoEnviado() || tipoImpressaoNotificacao.getRegistradoEmLote())) {
			List<Ait> aitList = new ListAitsCandidatosNaiFactory()
					.getEstrategiaListAits(TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey(), new SearchCriterios())
					.build(searchNotificacaoViaUnica(tipoImpressaoNotificacao.getCdAit()).getCriterios(), customConnection.getConnection());
			new GeraMovimentoNotificacaoFactory()
				.strategy(TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey())
				.gerarMovimentos(aitList, tipoImpressaoNotificacao.getCdUsuario());
			LoteImpressao loteNotificacaoNaiViaUnica = this.loteImpressaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), TipoLoteImpressaoEnum.LOTE_NAI.getKey());
			loteNotificacaoNaiViaUnica.setLgGeracaoViaUnica(true);
			this.loteImpressaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNaiViaUnica, tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			return this.loteImpressaoService.imprimirLoteImpressao(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), customConnection);
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
