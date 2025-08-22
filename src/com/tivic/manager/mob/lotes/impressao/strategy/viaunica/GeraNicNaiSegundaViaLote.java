package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraNicNaiSegundaViaLote extends TipoImpressaoNotificacaoHandler {
	private AitMovimentoRepository aitMovimentoRepository;
	private ILoteImpressaoService loteImpressaoService;
	private AitRepository aitRepository;
	
	public GeraNicNaiSegundaViaLote() throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (verificarAitNic(tipoImpressaoNotificacao.getCdAit(), customConnection)
				&& tipoImpressaoNotificacao.getContemMovimento() 
				&& tipoImpressaoNotificacao.getMovimentoEnviado()
				&& !tipoImpressaoNotificacao.getRegistradoEmLote()) {
			Ait ait = aitRepository.get(tipoImpressaoNotificacao.getCdAit());
			List<Ait> aitList = Arrays.asList(ait);
			LoteImpressao loteNotificacaoNaiViaUnica = this.loteImpressaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), TipoLoteImpressaoEnum.LOTE_NIC_NAI.getKey());
			loteNotificacaoNaiViaUnica.setLgGeracaoViaUnica(true);
			this.loteImpressaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNaiViaUnica, tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			return this.loteImpressaoService.imprimirLoteNotificacao(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), customConnection);
		} else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}

	}
	
	private boolean verificarAitNic(int cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.NIC_ENVIADA.getKey(), true);
		List<AitMovimento> aitMovimento = this.aitMovimentoRepository.find(searchCriterios, customConnection);
		if(aitMovimento.isEmpty()) {
			return false;
		}
		return true;
	}
}
