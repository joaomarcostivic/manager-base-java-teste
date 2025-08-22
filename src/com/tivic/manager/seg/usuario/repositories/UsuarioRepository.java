package com.tivic.manager.seg.usuario.repositories;

import java.util.List;

import com.tivic.manager.seg.Usuario;
import com.tivic.sol.connection.CustomConnection;

public interface UsuarioRepository {
	List<Usuario> getAll() throws Exception;
	List<Usuario> getAll(CustomConnection customConnection) throws Exception;
}
