package com.tivic.manager.msg;

public class NotificacaoGrupoUsuario {

	private int cdRegraNotificacao;
	private int cdGrupo;
	private int lgAtivo;
	private int lgEmail;

	public NotificacaoGrupoUsuario() { }

	public NotificacaoGrupoUsuario(int cdRegraNotificacao,
			int cdGrupo,
			int lgAtivo,
			int lgEmail) {
		setCdRegraNotificacao(cdRegraNotificacao);
		setCdGrupo(cdGrupo);
		setLgAtivo(lgAtivo);
		setLgEmail(lgEmail);
	}
	public void setCdRegraNotificacao(int cdRegraNotificacao){
		this.cdRegraNotificacao=cdRegraNotificacao;
	}
	public int getCdRegraNotificacao(){
		return this.cdRegraNotificacao;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public void setLgEmail(int lgEmail){
		this.lgEmail=lgEmail;
	}
	public int getLgEmail(){
		return this.lgEmail;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraNotificacao: " +  getCdRegraNotificacao();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		valueToString += ", lgEmail: " +  getLgEmail();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NotificacaoGrupoUsuario(getCdRegraNotificacao(),
			getCdGrupo(),
			getLgAtivo(),
			getLgEmail());
	}

}