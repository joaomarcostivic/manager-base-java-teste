package com.tivic.manager.rest.auth;

import java.io.Serializable;

public class Credencial implements Serializable {
	private static final long serialVersionUID = 4264453239022358431L;
	
	private String usuario;
	private String senha;
	private String modulo;
	
	public Credencial(String usuario, String senha, String modulo) {
		super();
		this.usuario = usuario;
		this.senha = senha;
		this.modulo = modulo;
	}
	
	public Credencial(String usuario, String senha) {
		super();
		this.usuario = usuario;
		this.senha = senha;
	}
	
	public Credencial() {
		super();
	}

	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	
	
}
