package com.tivic.manager.mob.ait.sync.entities;

public class UsuarioSyncDTO {

	private int cdUsuario;
	private String nmLogin;
	private int tpUsuario;

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

	public int getTpUsuario() {
		return tpUsuario;
	}

	public void setTpUsuario(int tpUsuario) {
		this.tpUsuario = tpUsuario;
	}
}
