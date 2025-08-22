package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.mob.ait.sync.entities.UsuarioSyncDTO;
import com.tivic.manager.seg.Usuario;

public class UsuarioSyncDTOBuilder {
	
	private UsuarioSyncDTO usuarioSyncDTO;
	
	public UsuarioSyncDTOBuilder(Usuario usuario) {
		this.usuarioSyncDTO = new UsuarioSyncDTO();
		setUsuarioSync(usuario);
	}
	
	private void setUsuarioSync(Usuario usuario) {
		this.usuarioSyncDTO.setCdUsuario(usuario.getCdUsuario());
		this.usuarioSyncDTO.setNmLogin(usuario.getNmLogin());
		this.usuarioSyncDTO.setTpUsuario(usuario.getTpUsuario());
	}

	public UsuarioSyncDTO build() {
		return this.usuarioSyncDTO;
	}
}
