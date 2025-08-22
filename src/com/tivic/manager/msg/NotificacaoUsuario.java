package com.tivic.manager.msg;

public class NotificacaoUsuario {

	private int cdRegraNotificacao;
	private int cdUsuario;
	private int lgAtivo;
	private int lgEmail;

	public NotificacaoUsuario() { }

	public NotificacaoUsuario(int cdRegraNotificacao,
			int cdUsuario,
			int lgAtivo,
			int lgEmail) {
		setCdRegraNotificacao(cdRegraNotificacao);
		setCdUsuario(cdUsuario);
		setLgAtivo(lgAtivo);
		setLgEmail(lgEmail);
	}
	public void setCdRegraNotificacao(int cdRegraNotificacao){
		this.cdRegraNotificacao=cdRegraNotificacao;
	}
	public int getCdRegraNotificacao(){
		return this.cdRegraNotificacao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
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
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		valueToString += ", lgEmail: " +  getLgEmail();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NotificacaoUsuario(getCdRegraNotificacao(),
			getCdUsuario(),
			getLgAtivo(),
			getLgEmail());
	}

}