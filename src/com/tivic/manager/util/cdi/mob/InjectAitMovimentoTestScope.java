package com.tivic.manager.util.cdi.mob;


import com.tivic.manager.mob.ait.circuitoait.CircuitoAit;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepositoryTest;
import com.tivic.manager.mob.aitmovimento.AitMovimentoService;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectAitMovimentoTestScope extends InjectScope {

	public InjectAitMovimentoTestScope(Scope scope) {
		super(scope);
	}

	@Override
	public void build() throws Exception {
		this.getScope().inject(AitMovimentoRepository.class, new AitMovimentoRepositoryTest());
		this.getScope().inject(IAitMovimentoService.class, new AitMovimentoService());
		this.getScope().inject(CircuitoAit.class, new CircuitoAit());
	}

}
