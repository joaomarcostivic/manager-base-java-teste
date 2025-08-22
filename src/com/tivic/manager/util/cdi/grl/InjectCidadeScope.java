package com.tivic.manager.util.cdi.grl;

import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.cidade.CidadeRepositoryDAO;
import com.tivic.manager.grl.cidade.CidadeService;
import com.tivic.manager.grl.cidade.ICidadeService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectCidadeScope extends InjectScope {
	
	public InjectCidadeScope(Scope scope) {
		super(scope);
	}
	
	@Override
	public void build() throws Exception {
		this.getScope().inject(CidadeRepository.class, new CidadeRepositoryDAO());
		this.getScope().inject(ICidadeService.class, new CidadeService());
	}
	
}
