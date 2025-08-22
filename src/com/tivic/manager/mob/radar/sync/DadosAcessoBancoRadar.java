package com.tivic.manager.mob.radar.sync;

import com.tivic.manager.util.Util;

public class DadosAcessoBancoRadar {

	private String driver;
	private String url;
	private String login;
	private String senha;
	private String nmOrgaoAutuador;
	
	public DadosAcessoBancoRadar() {
		this.driver = Util.getConfManager().getProps().getProperty("RADAR_DRIVER");
		this.url = Util.getConfManager().getProps().getProperty("RADAR_DBPATH");
		this.login = Util.getConfManager().getProps().getProperty("RADAR_LOGIN");
		this.senha = Util.getConfManager().getProps().getProperty("RADAR_PASS");
		this.nmOrgaoAutuador = Util.getConfManager().getProps().getProperty("RADAR_ORGAO_AUTUADOR");
	}
	
	public boolean possuiDadosCompletos() {
		return this.driver != null && this.url != null && this.login != null && this.senha != null && this.nmOrgaoAutuador != null;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getNmOrgaoAutuador() {
		return nmOrgaoAutuador;
	}
	
	public void setNmOrgaoAutuador(String nmOrgaoAutuador) {
		this.nmOrgaoAutuador = nmOrgaoAutuador;
	}
	
}