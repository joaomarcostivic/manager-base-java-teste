package com.tivic.manager.mob;

public class OrgaoServico {
	
	private int cdOrgaoServico;
	private int cdOrgao;
	private String idOrgaoServico;
	private String nmOrgaoServico;
	private int stOrgaoServico;
	private String nmUrlServico;
	private String nmLogin;
	private String nmSenha;
	
	public OrgaoServico() {}
	
	public OrgaoServico(
			int cdOrgaoServico, 
			int cdOrgao, 
			String idOrgaoServico, 
			String nmOrgaoServico,
			int stOrgaoServico, 
			String nmUrlServico, 
			String nmLogin, 
			String nmSenha) {
		this.cdOrgaoServico = cdOrgaoServico;
		this.cdOrgao = cdOrgao;
		this.idOrgaoServico = idOrgaoServico;
		this.nmOrgaoServico = nmOrgaoServico;
		this.stOrgaoServico = stOrgaoServico;
		this.nmUrlServico = nmUrlServico;
		this.nmLogin = nmLogin;
		this.nmSenha = nmSenha;
	}
	
	
	
	public int getCdOrgaoServico() {
		return cdOrgaoServico;
	}

	public void setCdOrgaoServico(int cdOrgaoServico) {
		this.cdOrgaoServico = cdOrgaoServico;
	}

	public int getCdOrgao() {
		return cdOrgao;
	}

	public void setCdOrgao(int cdOrgao) {
		this.cdOrgao = cdOrgao;
	}

	public String getIdOrgaoServico() {
		return idOrgaoServico;
	}

	public void setIdOrgaoServico(String idOrgaoServico) {
		this.idOrgaoServico = idOrgaoServico;
	}

	public String getNmOrgaoServico() {
		return nmOrgaoServico;
	}

	public void setNmOrgaoServico(String nmOrgaoServico) {
		this.nmOrgaoServico = nmOrgaoServico;
	}

	public int getStOrgaoServico() {
		return stOrgaoServico;
	}

	public void setStOrgaoServico(int stOrgaoServico) {
		this.stOrgaoServico = stOrgaoServico;
	}

	public String getNmUrlServico() {
		return nmUrlServico;
	}

	public void setNmUrlServico(String nmUrlServico) {
		this.nmUrlServico = nmUrlServico;
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

	public Object clone() {
		return new OrgaoServico(
				getCdOrgaoServico(),
				getCdOrgao(),
				getIdOrgaoServico(),
				getNmOrgaoServico(),
				getStOrgaoServico(),
				getNmUrlServico(),
				getNmLogin(),
				getNmSenha()
				);
				
	}

}
