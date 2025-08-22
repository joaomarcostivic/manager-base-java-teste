package com.tivic.manager.mob.servlets;

import javax.servlet.http.HttpServletRequest;

import sol.util.RequestUtilities;

public class DetranConsultaServletRequest {

	private int tpBusca;
	private String sgUf;
	private String nrPlaca;
	private String nmUsuario;
	private String nmSenha;
	private String idModulo;
	private String idOrgao;
	
	public DetranConsultaServletRequest() {
		// TODO Auto-generated constructor stub
	}

	public DetranConsultaServletRequest(HttpServletRequest request) {
		super();
		this.tpBusca = RequestUtilities.getParameterAsInteger(request, "tp", 0);
		this.sgUf = RequestUtilities.getParameterAsString(request, "sgUf", "BA");
		this.nrPlaca = RequestUtilities.getParameterAsString(request, "p", RequestUtilities.getParameterAsString(request, "nrPlaca", ""));
		this.nmUsuario = RequestUtilities.getParameterAsString(request, "u", "");
		this.nmSenha = RequestUtilities.getParameterAsString(request, "s", "");
		this.idModulo = RequestUtilities.getParameterAsString(request, "idModulo", "detran");
		this.idOrgao = RequestUtilities.getParameterAsString(request, "o", "");
	}
	
	public String getNmUsuario() {
		return nmUsuario;
	}
	
	public String getNmSenha() {
		return nmSenha;
	}
	
	public String getIdModulo() {
		return idModulo;
	}
	
	public String getNrPlaca() {
		return nrPlaca;
	}
	
	public void validate() throws Exception{
		if(nmUsuario==null || nmUsuario.equals("")) {
    		throw new Exception("{\n \"code\": \"-1\",\n \"message\": \"Nome do usuario nulo ou nao informado.\"\n}");	
    	}
    	else if(nmSenha==null || nmSenha.equals("")) {
    		throw new Exception("{\n \"code\": \"-2\",\n \"message\": \"Senha nula ou nao informada.\"\n}");	
    	}
    	else if(nrPlaca==null || nrPlaca.equals("")) {
    		throw new Exception("{\n \"code\": \"-3\",\n \"message\": \"Numero da placa nao informados.\"\n}");	
    	}
	}
	
	
	
}
