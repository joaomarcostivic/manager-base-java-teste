package com.tivic.manager.mob.lote.impressao.viaunica.nip;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraSegundaViaNIPLote extends TipoImpressaoNotificacaoNPHandler {

	private ILoteNotificacaoService loteNotificacaoService;
	private AitRepository aitRepository;
	private IAitMovimentoService aitMovimentoService;
	
	public GeraSegundaViaNIPLote() throws Exception {
		this.loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public NipImpressaoDTO gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (!tipoImpressaoNotificacao.getMovimentoAdvertencia() && tipoImpressaoNotificacao.getContemMovimento() && tipoImpressaoNotificacao.getMovimentoEnviado() && !(tipoImpressaoNotificacao.getRegistradoEmLote())) {
			Ait ait = aitRepository.get(tipoImpressaoNotificacao.getCdAit());
			List<Ait> aitList = Arrays.asList(ait);
			LoteImpressao loteNotificacaoNipViaUnica = this.loteNotificacaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), 
					getTipoLote(tipoImpressaoNotificacao.getCdAit(), customConnection));
			this.loteNotificacaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNipViaUnica.getCdLoteImpressao(), tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			byte[] arquivo = this.loteNotificacaoService.imprimirLoteNotificacao(loteNotificacaoNipViaUnica.getCdLoteImpressao(), customConnection);
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
		return !movimentos.isEmpty() ? TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey() : TipoLoteNotificacaoEnum.LOTE_NIP.getKey();
	}

}
