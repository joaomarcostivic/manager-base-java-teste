package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lotes.builders.impressao.viaunica.NipImpressaoDTOBuilder;
import com.tivic.manager.mob.lotes.dto.impressao.NipImpressaoDTO;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.impressao.strategy.TipoImpressaoNotificacaoNPHandler;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraSegundaViaNipLote extends TipoImpressaoNotificacaoNPHandler {

	private ILoteImpressaoService loteImpressaoService;
	private AitRepository aitRepository;
	private IAitMovimentoService aitMovimentoService;
	
	public GeraSegundaViaNipLote() throws Exception {
		this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public NipImpressaoDTO gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (!tipoImpressaoNotificacao.getMovimentoAdvertencia() && tipoImpressaoNotificacao.getContemMovimento() && tipoImpressaoNotificacao.getMovimentoEnviado() && !(tipoImpressaoNotificacao.getRegistradoEmLote())) {
			Ait ait = aitRepository.get(tipoImpressaoNotificacao.getCdAit());
			List<Ait> aitList = Arrays.asList(ait);
			LoteImpressao loteNotificacaoNipViaUnica = this.loteImpressaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), 
					getTipoLote(tipoImpressaoNotificacao.getCdAit(), customConnection));
			loteNotificacaoNipViaUnica.setLgGeracaoViaUnica(true);
			this.loteImpressaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNipViaUnica, tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			byte[] arquivo = this.loteImpressaoService.imprimirLoteNotificacao(loteNotificacaoNipViaUnica.getCdLoteImpressao(), customConnection);
			NipImpressaoDTO nipImpressaoDTO = new NipImpressaoDTOBuilder()
					.addCdAit(ait.getCdAit())
					.addArquivo(arquivo)
					.build();
			return nipImpressaoDTO;
		}
		else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}
	}
	
	private int getTipoLote(int cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.NIC_ENVIADA.getKey(), true);
		List<AitMovimento> movimentos = this.aitMovimentoService.find(searchCriterios, customConnection);
		return !movimentos.isEmpty() ? TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey() : TipoLoteImpressaoEnum.LOTE_NIP.getKey();
	}
	
}
