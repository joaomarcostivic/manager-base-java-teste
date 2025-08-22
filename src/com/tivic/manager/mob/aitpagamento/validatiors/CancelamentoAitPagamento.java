package com.tivic.manager.mob.aitpagamento.validatiors;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.aitpagamento.AitPagamentoRepository;
import com.tivic.manager.mob.aitpagamento.enums.SituacaoPagamentoEnum;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class CancelamentoAitPagamento implements Validator<AitPagamento> {
	private AitPagamentoRepository aitPagamentoRepository;
	
	public CancelamentoAitPagamento() throws Exception {
		this.aitPagamentoRepository = (AitPagamentoRepository) BeansFactory.get(AitPagamentoRepository.class);
		
	}

	@Override
	public void validate(AitPagamento aitPagamento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", aitPagamento.getCdAit());
		searchCriterios.addCriteriosEqualInteger("st_pagamento", SituacaoPagamentoEnum.CANCELADO.getKey());
		List<AitPagamento> aitPagamentos = this.aitPagamentoRepository.find(searchCriterios);
		if(!aitPagamentos.isEmpty() && aitPagamento.getCdAit() == aitPagamentos.get(0).getCdAit() && 
				aitPagamento.getCdPagamento() == aitPagamentos.get(0).getCdPagamento() &&
				aitPagamentos.get(0).getStPagamento() == SituacaoPagamentoEnum.CANCELADO.getKey()) {
			throw new BadRequestException("Este pagamento já foi cancelado");
		}
		
		
	}

}
