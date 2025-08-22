package com.tivic.manager.util.cdi.mob;

import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.ait.AitRepositoryTest;
import com.tivic.manager.mob.ait.AitService;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectAitTestScope extends InjectScope {

	public InjectAitTestScope(Scope scope) {
		super(scope);
	}

	@Override
	public void build() throws Exception {
		this.getScope().inject(AitRepository.class, new AitRepositoryTest());
		this.getScope().inject(IAitService.class, new AitService());
	}

}
