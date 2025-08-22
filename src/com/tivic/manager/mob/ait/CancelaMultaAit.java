package com.tivic.manager.mob.ait;

import java.sql.Types;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.CancelaMulta;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.CancelamentoMovimentoHandler;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class CancelaMultaAit extends CancelamentoMovimentoHandler {
	private AitMovimento aitMovimento;
	private IAitMovimentoService aitMovimentoService;
	
	public CancelaMultaAit(AitMovimento aitMovimento) throws Exception {
		this.aitMovimento = aitMovimento;
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	@Override
	public void gerar(CustomConnection customConnection) throws Exception {
		if(aitMovimentoService.find(getCriteriosMovimentoNIP(this.aitMovimento.getCdAit()), customConnection).isEmpty()) {
			nextGenerator.gerar(customConnection);
		} else {
			new CancelaMulta(this.aitMovimento.getCdAit(), this.aitMovimento, customConnection);
			nextGenerator.gerar(customConnection);
		}
	}

	private SearchCriterios getCriteriosMovimentoNIP(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriterios("tp_status", TipoStatusEnum.NIP_ENVIADA.getKey() + ","
			+ TipoStatusEnum.DEFESA_INDEFERIDA.getKey(), ItemComparator.IN, Types.INTEGER);
		return searchCriterios;
	}
}
