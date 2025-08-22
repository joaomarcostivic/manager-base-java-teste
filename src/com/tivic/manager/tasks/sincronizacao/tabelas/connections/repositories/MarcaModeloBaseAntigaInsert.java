package com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.str.MarcaModeloOldRepository;
import com.tivic.manager.tasks.sincronizacao.tabelas.adapters.IMarcaModeloAdapter;
import com.tivic.manager.tasks.sincronizacao.tabelas.adapters.MarcaModeloFtaAdapter;
import com.tivic.manager.tasks.sincronizacao.tabelas.adapters.MarcaModeloStrAdapter;
import com.tivic.manager.tasks.sincronizacao.tabelas.factories.ISincronizacaoMarcaModelo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class MarcaModeloBaseAntigaInsert implements ISincronizacaoMarcaModelo {
	
	private MarcaModeloOldRepository marcaModeloOldRepository;
	
	public MarcaModeloBaseAntigaInsert() throws Exception {
        this.marcaModeloOldRepository = (MarcaModeloOldRepository) BeansFactory.get(MarcaModeloOldRepository.class);
	}
	
	@Override
	public MarcaModelo get(int cdMarca, CustomConnection customConnection) throws Exception {
	    com.tivic.manager.str.MarcaModelo marcaModelo = marcaModeloOldRepository.get(cdMarca, customConnection);
	    if (marcaModelo == null) {
	        return null;
	    }
	    IMarcaModeloAdapter adapter = new MarcaModeloFtaAdapter(marcaModelo);
	    return adapter.toMarcaModeloFta();
	}

	@Override
	public void insert(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception {
        com.tivic.manager.str.MarcaModelo marcaModeloStr = convertToStr(marcaModelo);
		marcaModeloOldRepository.insert(marcaModeloStr, customConnection);
	}
	
	@Override
	public void update(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception {
        com.tivic.manager.str.MarcaModelo marcaModeloStr = convertToStr(marcaModelo);
		marcaModeloOldRepository.update(marcaModeloStr, customConnection);
	}

    private com.tivic.manager.str.MarcaModelo convertToStr(MarcaModelo marcaModelo) {
		IMarcaModeloAdapter adapter = new MarcaModeloStrAdapter(marcaModelo);
        return adapter.toMarcaModeloStr();
    }

}
