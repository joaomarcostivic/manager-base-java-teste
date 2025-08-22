package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.mob.ait.sync.entities.UsuarioSuporteSyncDTO;
import com.tivic.manager.seg.Usuario;

public class UsuarioSuporteSyncDTOBuilder {
	
	private UsuarioSuporteSyncDTO usuarioSuporteSyncDTO;
	
	public UsuarioSuporteSyncDTOBuilder(Usuario usuario) {
		this.usuarioSuporteSyncDTO = new UsuarioSuporteSyncDTO();
		setUsuarioSuporteSync(usuario);
	}
	
	private void setUsuarioSuporteSync(Usuario usuario) {
		this.usuarioSuporteSyncDTO.setCdUsuario(usuario.getCdUsuario());
		this.usuarioSuporteSyncDTO.setNmLogin(usuario.getNmLogin());
		this.usuarioSuporteSyncDTO.setNmSenha(usuario.getNmSenha());
	}

	public UsuarioSuporteSyncDTO build() {
		return this.usuarioSuporteSyncDTO;
	}
}
