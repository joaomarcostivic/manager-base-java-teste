package com.tivic.manager.util.cdi.ptc;

import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.documento.DocumentoRepositoryDAO;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;

public class InjectDocumentoScope extends InjectScope{

	public InjectDocumentoScope(Scope scope) {
		super(scope);
	}

	@Override
	public void build() throws Exception {
		this.getScope().inject(DocumentoRepository.class, new DocumentoRepositoryDAO());
	}

}
