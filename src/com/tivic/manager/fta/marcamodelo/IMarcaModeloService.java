package com.tivic.manager.fta.marcamodelo;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.sol.connection.CustomConnection;

public interface IMarcaModeloService {
	public MarcaModelo getByNrMarca(String nrMarca) throws Exception;
	public MarcaModelo get(int cdMarca) throws Exception;
	public MarcaModelo get(int cdMarca, CustomConnection customConnection) throws Exception;
}
