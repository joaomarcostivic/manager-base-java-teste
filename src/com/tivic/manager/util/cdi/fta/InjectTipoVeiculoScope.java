package com.tivic.manager.util.cdi.fta;

import com.tivic.manager.fta.tipoveiculo.ITipoVeiculoService;
import com.tivic.manager.fta.tipoveiculo.TipoVeiculoRepository;
import com.tivic.manager.fta.tipoveiculo.TipoVeiculoRepositoryDAO;
import com.tivic.manager.fta.tipoveiculo.TipoVeiculoService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectTipoVeiculoScope extends InjectScope {
	
	public InjectTipoVeiculoScope(Scope scope) {
		super(scope);
	}
	
	@Override
	public void build() throws Exception {
		this.getScope().inject(TipoVeiculoRepository.class, new TipoVeiculoRepositoryDAO());
		this.getScope().inject(ITipoVeiculoService.class, new TipoVeiculoService());
	}
	
}
