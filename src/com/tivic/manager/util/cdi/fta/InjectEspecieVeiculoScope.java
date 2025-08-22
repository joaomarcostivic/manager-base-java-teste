package com.tivic.manager.util.cdi.fta;

import com.tivic.manager.fta.especieveiculo.EspecieVeiculoRepository;
import com.tivic.manager.fta.especieveiculo.EspecieVeiculoRepositoryDAO;
import com.tivic.manager.fta.especieveiculo.EspecieVeiculoService;
import com.tivic.manager.fta.especieveiculo.IEspecieVeiculoService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectEspecieVeiculoScope extends InjectScope {
	
	public InjectEspecieVeiculoScope(Scope scope) {
		super(scope);
	}
	
	@Override
	public void build() throws Exception {
		this.getScope().inject(EspecieVeiculoRepository.class, new EspecieVeiculoRepositoryDAO());
		this.getScope().inject(IEspecieVeiculoService.class, new EspecieVeiculoService());
	}
	
}
