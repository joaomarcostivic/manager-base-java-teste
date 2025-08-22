package com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.marcamodelo.MarcaModeloRepository;
import com.tivic.manager.tasks.sincronizacao.tabelas.factories.ISincronizacaoMarcaModelo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class MarcaModeloFtaInsert implements ISincronizacaoMarcaModelo {
	
    private MarcaModeloRepository marcaModeloRepository;
	
	public MarcaModeloFtaInsert() throws Exception {
        this.marcaModeloRepository = (MarcaModeloRepository) BeansFactory.get(MarcaModeloRepository.class);
	}
	
	@Override
	public MarcaModelo get(int cdMarca, CustomConnection customConnection) throws Exception {
		return marcaModeloRepository.getBaseNova(cdMarca, customConnection);
	}
	
	@Override
	public void insert(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception {
	    marcaModeloRepository.insert(marcaModelo, customConnection);
	}
	
	@Override
	public void update(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception {
	    marcaModeloRepository.update(marcaModelo, customConnection);
	}
}
