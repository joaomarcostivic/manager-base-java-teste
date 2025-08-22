package com.tivic.manager.mob.acaousuario;

import com.tivic.sol.connection.CustomConnection;

public interface AcaoUsuarioRepository {
	
	public void insert(AcaoUsuario acaoUsuario, CustomConnection customConnection) throws Exception;
	public void update(AcaoUsuario acaoUsuario, CustomConnection customConnection) throws Exception;
	public AcaoUsuario get(int cdAcao) throws Exception;
	public AcaoUsuario get(int cdAcao, CustomConnection customConnection) throws Exception;
}
