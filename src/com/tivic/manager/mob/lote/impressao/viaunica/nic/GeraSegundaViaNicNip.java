package com.tivic.manager.mob.lote.impressao.viaunica.nic;

import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.lote.impressao.GerarDocumentoSegundaViaFactory;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.lote.impressao.viaunica.nip.NipImpressaoDTO;
import com.tivic.manager.mob.lote.impressao.viaunica.nip.NipImpressaoDTOBuilder;
import com.tivic.manager.mob.lote.impressao.viaunica.nip.TipoImpressaoNotificacaoNPHandler;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraSegundaViaNicNip extends TipoImpressaoNotificacaoNPHandler {
	
	private AitMovimentoRepository aitMovimentoRepository;
	
	public GeraSegundaViaNicNip() throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	@Override
	public NipImpressaoDTO gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		Boolean printPortal = false;
		if (!tipoImpressaoNotificacao.getMovimentoAdvertencia() && tipoImpressaoNotificacao.getContemMovimento() 
				&& tipoImpressaoNotificacao.getMovimentoEnviado() 
				&& tipoImpressaoNotificacao.getRegistradoEmLote()
				&& verificarAitNic(tipoImpressaoNotificacao.getCdAit(), customConnection)) {
			byte[] arquivo = new GerarDocumentoSegundaViaFactory()
					.getStrategy(TipoLoteNotificacaoEnum.LOTE_NIC_NIP_VIA_UNICA.getKey())
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
