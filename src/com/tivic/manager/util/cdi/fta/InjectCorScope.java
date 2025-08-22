package com.tivic.manager.util.cdi.fta;

import com.tivic.manager.fta.cor.CorRepository;
import com.tivic.manager.fta.cor.CorRepositoryDAO;
import com.tivic.manager.fta.cor.CorService;
import com.tivic.manager.fta.cor.ICorService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectCorScope extends InjectScope {
	
	public InjectCorScope(Scope scope) {
		super(scope);
	}
	
	@Override
	public void build() throws Exception {
		this.getScope().inject(CorRepository.class, new CorRepositoryDAO());
		this.getScope().inject(ICorService.class, new CorService());
	}
	
}
