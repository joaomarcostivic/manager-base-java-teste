package com.tivic.manager.mob.ait;

import java.sql.Types;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.CancelamentoMovimentoHandler;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class MovimentoRegistrado extends CancelamentoMovimentoHandler{
	
	private IAitMovimentoService aitMovimentoService;
	private CustomConnection customConnection;
	private int cdAit;
	
	public MovimentoRegistrado(int cdAit, CustomConnection customConnection) throws Exception {
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.cdAit = cdAit;
		this.customConnection = customConnection;
	}

	@Override
	public void gerar(CustomConnection customConnection) throws Exception {
		if(this.aitMovimentoService.find(getCriteriosMovimentoRegistrado(this.cdAit), this.customConnection).isEmpty()) {
			throw new Exception("Nenhum Movimento encontrado");
		} else {
			nextGenerator.gerar(this.customConnection);
		}
	}

	private SearchCriterios getCriteriosMovimentoRegistrado(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", AitMovimentoServices.REGISTRO_INFRACAO, true);
		searchCriterios.addCriterios("A.lg_enviado_detran", String.valueOf(AitMovimentoServices.REGISTRADO) + ","
			+ String.valueOf(AitMovimentoServices.NAO_ENVIAR),  ItemComparator.IN, Types.INTEGER);
		return searchCriterios;
	}
}
