package com.tivic.manager.mob.ait.sync.entities;

public class UsuarioSuporteSyncDTO {

	private int cdUsuario;
	private String nmLogin;
	private String nmSenha;

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getNmLogin() {
		return nmLogin;
	}

	public void setNmLogin(String nmLogin) {
		this.nmLogin = nmLogin;
	}

	public String getNmSenha() {
		return nmSenha;
	}
	
	public void setNmSenha(String nmSenha) {
		this.nmSenha = nmSenha;
	}
}
