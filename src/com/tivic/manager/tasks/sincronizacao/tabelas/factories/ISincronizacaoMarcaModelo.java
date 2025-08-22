package com.tivic.manager.tasks.sincronizacao.tabelas.factories;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.sol.connection.CustomConnection;

public interface ISincronizacaoMarcaModelo {

	public MarcaModelo get(int cdMarca, CustomConnection customConnection) throws Exception;
	public void insert(MarcaModelo existingMarcaModelo, CustomConnection customConnection) throws Exception;
	public void update(MarcaModelo table, CustomConnection customConnection) throws Exception;
	
}
