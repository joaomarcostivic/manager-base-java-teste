package com.tivic.manager.mob.acaousuario;

import com.tivic.sol.connection.CustomConnection;

public class AcaoUsuarioRepositoryDAO implements AcaoUsuarioRepository {

	@Override
	public void insert(AcaoUsuario acaoUsuario, CustomConnection customConnection) throws Exception {
		AcaoUsuarioDAO.insert(acaoUsuario, customConnection.getConnection());
	}

	@Override
	public void update(AcaoUsuario acaoUsuario, CustomConnection customConnection) throws Exception {
		AcaoUsuarioDAO.update(acaoUsuario, customConnection.getConnection());
	}

	@Override
	public AcaoUsuario get(int cdAcao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			AcaoUsuario acaoUsuario = get(cdAcao, customConnection);
			customConnection.finishConnection();
			return acaoUsuario;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public AcaoUsuario get(int cdAcao, CustomConnection customConnection) throws Exception {
		return AcaoUsuarioDAO.get(cdAcao, customConnection.getConnection());
	}
}
