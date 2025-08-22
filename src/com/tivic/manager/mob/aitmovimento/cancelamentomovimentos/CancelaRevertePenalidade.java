package com.tivic.manager.mob.aitmovimento.cancelamentomovimentos;

import java.util.List;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.exceptions.AitEmptyGeracaoPrazoDefesaNip;
import com.tivic.manager.mob.aitmovimento.validators.ValidationCancelamentoNip;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.lote.impressao.GerarMovimentoNotificacaoFactory;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CancelaRevertePenalidade implements ICancelaRevertePenalidade{
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private AitRepository aitRepository;
	private IAitMovimentoService aitMovimentoService;

	public CancelaRevertePenalidade() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
	}
	
	@Override
	public void cancelarEReverterPenalidade(AitMovimento aitMovimento, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new ValidationCancelamentoNip(aitMovimento).validate(customConnection);
			new CancelaPenalidade(aitMovimento, cdUsuario, customConnection);
			removerAitLoteImpressao(aitMovimento.getCdAit(), customConnection);
			gerarFimPrazoDefesaENip(aitMovimento.getCdAit(), cdUsuario);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void removerAitLoteImpressao (int cdAit, CustomConnection customConnection) throws Exception {
		LoteImpressaoAit loteImpressaoAit = getLoteImpressaoAit(cdAit);
		if (loteImpressaoAit != null) {
			loteImpressaoAitRepository.delete(loteImpressaoAit, customConnection);
		}
	}

	private LoteImpressaoAit getLoteImpressaoAit(int cdAit) throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios(); 
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("B.tp_documento", TipoLoteNotificacaoEnum.LOTE_NIP.getKey());
		Search<LoteImpressaoAit> search = new SearchBuilder<LoteImpressaoAit>(" mob_lote_impressao_ait A ")
				.fields(" A.* ")
				.addJoinTable(" JOIN mob_lote_impressao B ON (A.cd_lote_impressao = B.cd_lote_impressao) ")
				.searchCriterios(searchCriterios)
				.build();
		List<LoteImpressaoAit> loteImpressaoAitList = search.getList(LoteImpressaoAit.class);
		if(loteImpressaoAitList.isEmpty() || loteImpressaoAitList == null) {
			return null;
		}
		return loteImpressaoAitList.get(0);
	}
	
	private void gerarFimPrazoDefesaENip (int cdAit, int cdUsuario) throws AitEmptyGeracaoPrazoDefesaNip, Exception {
		SearchCriterios searchCriterios = new SearchCriterios(); 
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		List<Ait> aitList  = aitRepository.find(searchCriterios);
		if (aitList.isEmpty() || aitList == null) {
			throw new AitEmptyGeracaoPrazoDefesaNip();
		}
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey(), true);
		List<AitMovimento> notificacaoAdvertencia = this.aitMovimentoService.find(searchCriterios);
		if (notificacaoAdvertencia.get(0).getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey()) 
			new GerarMovimentoNotificacaoFactory().strategy(TipoLoteNotificacaoEnum.LOTE_FIM_PRAZO_DEFESA_VIA_UNICA.getKey()).gerarMovimentos(aitList, cdUsuario);
		
		new GerarMovimentoNotificacaoFactory().strategy(TipoLoteNotificacaoEnum.LOTE_NIP_VIA_UNICA.getKey()).gerarMovimentos(aitList, cdUsuario);
	}
	
}
