package com.tivic.manager.util.cdi.fta;

import com.tivic.manager.fta.categoriaveiculo.CategoriaVeiculoRepository;
import com.tivic.manager.fta.categoriaveiculo.CategoriaVeiculoRepositoryDAO;
import com.tivic.manager.fta.categoriaveiculo.CategoriaVeiculoService;
import com.tivic.manager.fta.categoriaveiculo.ICategoriaVeiculoService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectCategoriaVeiculoScope extends InjectScope {
	
	public InjectCategoriaVeiculoScope(Scope scope) {
		super(scope);
	}
	
	@Override
	public void build() throws Exception {
		this.getScope().inject(CategoriaVeiculoRepository.class, new CategoriaVeiculoRepositoryDAO());
		this.getScope().inject(ICategoriaVeiculoService.class, new CategoriaVeiculoService());
	}
	
}
