package com.tivic.manager.str.endereco.enderecoboat;

import com.tivic.sol.connection.CustomConnection;

public class EnderecoBoatRepositoryDAO implements EnderecoBoatRepository {

	@Override
	public void insert(EnderecoBoat enderecoBoat, CustomConnection customConnection) throws Exception {
		int insert = EnderecoBoatDAO.insert(enderecoBoat, customConnection.getConnection());
		if(insert <= 0) {
			throw new Exception("Não foi possível inserir EnderecoBoat.");
		}
	}

	@Override
	public EnderecoBoat get(int cd_endereco, int cd_boat, CustomConnection customConnection) throws Exception {
		EnderecoBoat enderecoBoat = EnderecoBoatDAO.get(cd_endereco, cd_boat, customConnection.getConnection());
		return enderecoBoat;
	}
}
