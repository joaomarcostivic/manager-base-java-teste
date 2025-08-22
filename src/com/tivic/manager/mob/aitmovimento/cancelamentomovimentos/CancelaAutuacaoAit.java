package com.tivic.manager.mob.aitmovimento.cancelamentomovimentos;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.CancelaAutuacao;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class CancelaAutuacaoAit extends CancelamentoMovimentoHandler {
	private IAitMovimentoService aitMovimentoService;
	private AitMovimento aitMovimento;
	
	public CancelaAutuacaoAit(AitMovimento aitMovimento) throws Exception {
		this.aitMovimento = aitMovimento;
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	@Override
	public void gerar(CustomConnection customConnection) throws Exception {		
		if(aitMovimentoService.find(getCriteriosMovimentoNAI(this.aitMovimento.getCdAit()), customConnection).isEmpty()) {
			nextGenerator.gerar(customConnection);
		} else {
			new CancelaAutuacao(this.aitMovimento, customConnection);
			nextGenerator.gerar(customConnection);
		}
	}
	
	private SearchCriterios getCriteriosMovimentoNAI(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.NAI_ENVIADO.getKey(), true);
		return searchCriterios;
	}
}
