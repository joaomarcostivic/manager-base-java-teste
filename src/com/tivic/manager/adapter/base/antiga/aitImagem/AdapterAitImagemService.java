package com.tivic.manager.adapter.base.antiga.aitImagem;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterAitImagemService implements IAdapterService<AitImagemOld, AitImagem> {
	
	@Override
	public AitImagemOld toBaseAntiga(AitImagem aitImagem) throws Exception {
		if (aitImagem == null) 
			throw new ValidacaoException("Uma imagem valida deve ser informado para a conversão.");
		return new AdapterAitImagem().adapterToOld(aitImagem);
	}

	@Override
	public AitImagem toBaseNova(AitImagemOld aitImagem) throws Exception {
		if (aitImagem == null) 
			throw new ValidacaoException("Uma imagem valida deve ser informado para a conversão.");
		return new AdapterAitImagem().adapterToBaseNova(aitImagem);
	}
	
}
