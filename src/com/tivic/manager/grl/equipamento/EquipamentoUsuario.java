package com.tivic.manager.grl.equipamento;

import com.tivic.manager.seg.Usuario;

public class EquipamentoUsuario extends Equipamento {

	private Usuario usuario;
	
	public EquipamentoUsuario() {
		
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
}
