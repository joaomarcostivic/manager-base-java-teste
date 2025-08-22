package com.tivic.manager.util.cdi.mob;

import com.tivic.manager.mob.ocorrencia.OcorrenciaRepository;
import com.tivic.manager.mob.ocorrencia.OcorrenciaRepositoryDAO;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectOcorrenciaTestScope extends InjectScope {

	public InjectOcorrenciaTestScope(Scope scope) {
		super(scope);
	}

	@Override
	public void build() throws Exception {
		this.getScope().inject(OcorrenciaRepository.class, new OcorrenciaRepositoryDAO());
	}

}
