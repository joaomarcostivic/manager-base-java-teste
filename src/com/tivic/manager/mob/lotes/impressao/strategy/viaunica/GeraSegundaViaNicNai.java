package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.factory.impressao.DocumentSegundaViaGeneratorFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraSegundaViaNicNai extends TipoImpressaoNotificacaoHandler {

	private AitMovimentoRepository aitMovimentoRepository;
	
	public GeraSegundaViaNicNai() throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	@Override
	public byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		Boolean printPortal = false;
		if (tipoImpressaoNotificacao.getContemMovimento()
				&& tipoImpressaoNotificacao.getMovimentoEnviado() 
				&& tipoImpressaoNotificacao.getRegistradoEmLote()
				&& verificarAitNic(tipoImpressaoNotificacao.getCdAit(), customConnection)) {
			return new DocumentSegundaViaGeneratorFactory()
					.getStrategy(TipoLoteImpressaoEnum.LOTE_NIC_NAI_VIA_UNICA.getKey())
					.gerarDocumentos(tipoImpressaoNotificacao.getCdAit(), printPortal, customConnection);
		} 
		else {
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
