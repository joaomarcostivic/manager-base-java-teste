package com.tivic.manager.mob.lote.impressao.viaunica.nai;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacaoHandler;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.GeradorImpressaoNIC;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.NicSimplificadaDTO;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.NicSimplificadoDTOBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraNICNaiLote extends TipoImpressaoNotificacaoHandler {
	private AitMovimentoRepository aitMovimentoRepository;
	private ILoteNotificacaoService loteNotificacaoService;
	private AitRepository aitRepository;
	
	public GeraNICNaiLote() throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
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
			LoteImpressao loteNotificacaoNaiViaUnica = this.loteNotificacaoService.gerarLoteNotificacaoAitViaUnica(aitList,
					tipoImpressaoNotificacao.getCdUsuario(), TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey());			
			NicSimplificadaDTO nicImpressaoDTO = new NicSimplificadoDTOBuilder()
					.addCdAit(tipoImpressaoNotificacao.getCdAit())
					.addCdLoteImpressao(loteNotificacaoNaiViaUnica.getCdLoteImpressao())
					.build();
			new GeradorImpressaoNIC().geraDocumentos(nicImpressaoDTO, tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			return this.loteNotificacaoService.imprimirLoteNotificacao(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), customConnection);

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
