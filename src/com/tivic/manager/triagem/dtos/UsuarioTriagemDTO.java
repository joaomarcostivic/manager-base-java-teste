package com.tivic.manager.triagem.dtos;

public class UsuarioTriagemDTO {

    private Integer cdUsuario;
    private String nmUsuario;
    private String nmEmail;
    private String nmSenha;
    private String nmRole;
    private String nmLogin;
    private Integer stUsuario;
    private ArquivoUsuarioTriagem imagemPerfil;
    
	public Integer getCdUsuario() {
		return cdUsuario;
	}
	public void setCdUsuario(Integer cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	public String getNmUsuario() {
		return nmUsuario;
	}
	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}
	public String getNmEmail() {
		return nmEmail;
	}
	public void setNmEmail(String nmEmail) {
		this.nmEmail = nmEmail;
	}
	public String getNmSenha() {
		return nmSenha;
	}
	public void setNmSenha(String nmSenha) {
		this.nmSenha = nmSenha;
	}
	public String getNmRole() {
		return nmRole;
	}
	public void setNmRole(String nmRole) {
		this.nmRole = nmRole;
	}
	public String getNmLogin() {
		return nmLogin;
	}
	public void setNmLogin(String nmLogin) {
		this.nmLogin = nmLogin;
	}
	public Integer getStUsuario() {
		return stUsuario;
	}
	public void setStUsuario(Integer stUsuario) {
		this.stUsuario = stUsuario;
	}
	public ArquivoUsuarioTriagem getImagemPerfil() {
		return imagemPerfil;
	}
	public void setImagemPerfil(ArquivoUsuarioTriagem imagemPerfil) {
		this.imagemPerfil = imagemPerfil;
	}
    
}
