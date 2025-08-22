package com.tivic.manager.util.cdi.mob;

import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.mob.infracao.InfracaoRepositoryDAO;
import com.tivic.manager.mob.infracao.InfracaoService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;
import com.tivic.manager.mob.infracao.IInfracaoService;

public class InjectInfracaoScope extends InjectScope {
	
	public InjectInfracaoScope(Scope scope) {
		super(scope);
	}
	
	@Override
	public void build() throws Exception {
		this.getScope().inject(InfracaoRepository.class, new InfracaoRepositoryDAO());
		this.getScope().inject(IInfracaoService.class, new InfracaoService());
	}
	
}
