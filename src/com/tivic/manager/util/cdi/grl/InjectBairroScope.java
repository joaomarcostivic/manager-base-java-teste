package com.tivic.manager.util.cdi.grl;

import com.tivic.manager.grl.bairro.IBairroService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;
import com.tivic.manager.grl.bairro.BairroRepository;
import com.tivic.manager.grl.bairro.BairroRepositoryDAO;
import com.tivic.manager.grl.bairro.BairroService;

public class InjectBairroScope extends InjectScope {
	
	public InjectBairroScope(Scope scope) {
		super(scope);
	}
	
	@Override
	public void build() throws Exception {
		this.getScope().inject(BairroRepository.class, new BairroRepositoryDAO());
		this.getScope().inject(IBairroService.class, new BairroService());
	}
	
}
