package com.tivic.manager.adapter.base.antiga.usuario;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterUsuarioService implements IAdapterService<UsuarioOld, Usuario> {

	@Override
	public UsuarioOld toBaseAntiga(Usuario usuario) throws ValidacaoException {
		if (usuario == null)
			throw new ValidacaoException("Um usuário válido deve ser informado para a conversão.");
		return new AdapterUsuario().toBaseAntiga(usuario);
	}

	@Override
	public Usuario toBaseNova(UsuarioOld usuarioOld) throws Exception {
		if (usuarioOld == null)
			throw new ValidacaoException("Um usuário válido deve ser informado para a conversão.");
		return new AdapterUsuario().toBaseNova(usuarioOld);
	}
	
}
