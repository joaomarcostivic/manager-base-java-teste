package com.tivic.manager.mob.servlets;

public class ArquivoConfiguracaoDetranServlet {

	private String urlConsulta;
	private String urlBdv;
	private String codigoUsuario;
	private String senhaUsuario;
	private String codigoOperacao;
	
	public ArquivoConfiguracaoDetranServlet() {
		urlConsulta = "http://200.187.13.98/wsdetranconsulta/wsdetranconsulta.asmx";
		urlBdv = "http://radar.tivic.com.br:82/etransito/rws/bdv/veiculo";
		codigoUsuario = "100876";
		senhaUsuario = "TVIC14";
		codigoOperacao = "CON";
	}

	public String getUrlConsulta() {
		return urlConsulta;
	}

	public void setUrlConsulta(String urlConsulta) {
		this.urlConsulta = urlConsulta;
	}

	public String getUrlBdv() {
		return urlBdv;
	}

	public void setUrlBdv(String urlBdv) {
		this.urlBdv = urlBdv;
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getSenhaUsuario() {
		return senhaUsuario;
	}

	public void setSenhaUsuario(String senhaUsuario) {
		this.senhaUsuario = senhaUsuario;
	}

	public String getCodigoOperacao() {
		return codigoOperacao;
	}

	public void setCodigoOperacao(String codigoOperacao) {
		this.codigoOperacao = codigoOperacao;
	}
	
	
}
