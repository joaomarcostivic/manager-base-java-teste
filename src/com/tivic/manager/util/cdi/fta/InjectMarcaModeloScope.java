package com.tivic.manager.util.cdi.fta;

import com.tivic.manager.fta.marcamodelo.MarcaModeloRepository;
import com.tivic.manager.fta.marcamodelo.MarcaModeloRepositoryDAO;
import com.tivic.manager.fta.marcamodelo.MarcaModeloService;
import com.tivic.sol.cdi.InjectScope;
import com.tivic.sol.cdi.Scope;
import com.tivic.manager.fta.marcamodelo.IMarcaModeloService;

public class InjectMarcaModeloScope extends InjectScope {
	
	public InjectMarcaModeloScope(Scope scope) {
		super(scope);
	}
	
	@Override
	public void build() throws Exception {
		this.getScope().inject(MarcaModeloRepository.class, new MarcaModeloRepositoryDAO());
		this.getScope().inject(IMarcaModeloService.class, new MarcaModeloService());
	}
	
}
