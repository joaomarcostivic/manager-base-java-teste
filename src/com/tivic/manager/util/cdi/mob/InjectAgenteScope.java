package com.tivic.manager.util.cdi.mob;

import com.tivic.manager.mob.agente.AgenteRepository;
import com.tivic.manager.mob.agente.AgenteRepositoryDAO;
import com.tivic.manager.mob.agente.AgenteService;
import com.tivic.manager.mob.agente.IAgenteService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectAgenteScope extends InjectScope {
	
	public InjectAgenteScope(Scope scope) {
		super(scope);
	}
	
	@Override
	public void build() throws Exception {
		this.getScope().inject(AgenteRepository.class, new AgenteRepositoryDAO());
		this.getScope().inject(IAgenteService.class, new AgenteService());
	}
	
}
