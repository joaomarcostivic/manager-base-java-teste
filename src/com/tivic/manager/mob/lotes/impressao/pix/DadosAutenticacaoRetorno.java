package com.tivic.manager.mob.lotes.impressao.pix;

public class DadosAutenticacaoRetorno {
	private String usuario;
	private String token;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
	    return "{\"usuario\": \"" + getUsuario() + "\""
	            + ", \"token\": \"" + getToken() + "\""	
	            + "}";
	}
}
