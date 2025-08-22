package com.tivic.manager.mob.acaousuario;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;

public interface IAcaoUsuarioService {

	public void insert(AcaoUsuario acaoUsuario) throws Exception;
	public void insert(AcaoUsuario acaoUsuario, CustomConnection customConnection) throws Exception;
	public void insertAll(List<AcaoUsuarioDTO> acaoUsuarioList) throws Exception;
	public void insertAll(List<AcaoUsuarioDTO> acaoUsuarioList, CustomConnection customConnection) throws Exception;
	public void update(AcaoUsuario acaoUsuario) throws Exception;
	public void update(AcaoUsuario acaoUsuario, CustomConnection customConnection) throws Exception;
	public AcaoUsuario get(int cdAcao) throws Exception;
	public AcaoUsuario get(int cdAcao, CustomConnection customConnection) throws Exception;
}
