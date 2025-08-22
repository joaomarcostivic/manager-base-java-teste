package com.tivic.manager.util.radar;

import java.sql.Connection;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.sol.connection.creater.CreaterConnectionCustom;

public class BancoRadarConnection {

	private String url;
	private String login;
	private String senha;
	private String nmOrgaoAutuador;
	
	public BancoRadarConnection() {
		this.url = ManagerConf.getInstance().get("RADAR_DBPATH");;
		this.login = ManagerConf.getInstance().get("RADAR_LOGIN");
		this.senha = ManagerConf.getInstance().get("RADAR_PASS");
		this.nmOrgaoAutuador = ManagerConf.getInstance().get("RADAR_ORGAO_AUTUADOR");
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
	
	private void verifyRadarParams() throws Exception {
		if(this.url == null || this.login == null || this.senha == null || this.nmOrgaoAutuador == null) {
			throw new Exception("Não foi possível sincronizar com o servidor do radar. Valores de configurações inexistentes.");
		}
	}
	
	public Connection getConnection() throws Exception {
		this.verifyRadarParams();
		CreaterConnectionCustom createrConnectionCustom = new CreaterConnectionCustom(this.url, this.login, this.senha);
		return createrConnectionCustom.create();
	}
	
}