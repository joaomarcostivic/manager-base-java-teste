package com.tivic.manager.util.cdi.mob;

import com.tivic.manager.mob.arquivomovimento.ArquivoMovimentoRepository;
import com.tivic.manager.mob.arquivomovimento.ArquivoMovimentoRepositoryTest;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectArquivoMovimentoTestScope extends InjectScope {

	public InjectArquivoMovimentoTestScope(Scope scope) {
		super(scope);
	}

	@Override
	public void build() throws Exception {
		this.getScope().inject(ArquivoMovimentoRepository.class, new ArquivoMovimentoRepositoryTest());
	}

}
