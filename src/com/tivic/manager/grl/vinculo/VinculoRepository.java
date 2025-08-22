package com.tivic.manager.grl.vinculo;

import java.util.List;

import com.tivic.manager.grl.Vinculo;
import com.tivic.sol.connection.CustomConnection;

public interface VinculoRepository {
	 Vinculo get(int cdVinculo) throws Exception;
	 Vinculo get(int cdVinculo, CustomConnection customConnection) throws Exception;
	 List<Vinculo> find() throws Exception;
	 List<Vinculo> find(CustomConnection customConnection) throws Exception;
	 
}
