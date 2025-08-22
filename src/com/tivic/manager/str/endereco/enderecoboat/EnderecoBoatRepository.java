package com.tivic.manager.str.endereco.enderecoboat;

import com.tivic.sol.connection.CustomConnection;

public interface EnderecoBoatRepository {

	void insert(EnderecoBoat enderecoBoat, CustomConnection customConnection) throws Exception;
	EnderecoBoat get(int cd_endereco, int cd_boat, CustomConnection customConnection) throws Exception;
}
