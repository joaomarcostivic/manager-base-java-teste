package com.tivic.manager.msg;

import java.util.GregorianCalendar;

public class Notificacao {
	
	public static int ALERTA = 0;
	public static int INFO = 1;
	public static int ERRO = 2;
	public static int MENSAGEM = 3;
	public static int SUPORTE = 4;

	private int cdNotificacao;
	private int cdUsuario;
	private String dsAssunto;
	private int tpNotificacao;
	private String txtNotificacao;
	private GregorianCalendar dtNotificacao;
	private GregorianCalendar dtLeitura;
	private int cdMensagem;
	private int cdRegraNotificacao;
	private String txtObjeto;

	public Notificacao() { }

	public Notificacao(int cdNotificacao,
			int cdUsuario,
			String dsAssunto,
			int tpNotificacao,
			String txtNotificacao,
			GregorianCalendar dtNotificacao,
			GregorianCalendar dtLeitura,
			int cdMensagem,
			int cdRegraNotificacao,
			String txtObjeto) {
		setCdNotificacao(cdNotificacao);
		setCdUsuario(cdUsuario);
		setDsAssunto(dsAssunto);
		setTpNotificacao(tpNotificacao);
		setTxtNotificacao(txtNotificacao);
		setDtNotificacao(dtNotificacao);
		setDtLeitura(dtLeitura);
		setCdMensagem(cdMensagem);
		setCdRegraNotificacao(cdRegraNotificacao);
		setTxtObjeto(txtObjeto);
	}
	public void setCdNotificacao(int cdNotificacao){
		this.cdNotificacao=cdNotificacao;
	}
	public int getCdNotificacao(){
		return this.cdNotificacao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDsAssunto(String dsAssunto){
		this.dsAssunto=dsAssunto;
	}
	public String getDsAssunto(){
		return this.dsAssunto;
	}
	public void setTpNotificacao(int tpNotificacao){
		this.tpNotificacao=tpNotificacao;
	}
	public int getTpNotificacao(){
		return this.tpNotificacao;
	}
	public void setTxtNotificacao(String txtNotificacao){
		this.txtNotificacao=txtNotificacao;
	}
	public String getTxtNotificacao(){
		return this.txtNotificacao;
	}
	public void setDtNotificacao(GregorianCalendar dtNotificacao){
		this.dtNotificacao=dtNotificacao;
	}
	public GregorianCalendar getDtNotificacao(){
		return this.dtNotificacao;
	}
	public void setDtLeitura(GregorianCalendar dtLeitura){
		this.dtLeitura=dtLeitura;
	}
	public GregorianCalendar getDtLeitura(){
		return this.dtLeitura;
	}
	public void setCdMensagem(int cdMensagem){
		this.cdMensagem=cdMensagem;
	}
	public int getCdMensagem(){
		return this.cdMensagem;
	}
	public void setCdRegraNotificacao(int cdRegraNotificacao){
		this.cdRegraNotificacao=cdRegraNotificacao;
	}
	public int getCdRegraNotificacao(){
		return this.cdRegraNotificacao;
	}
	public void setTxtObjeto(String txtObjeto){
		this.txtObjeto=txtObjeto;
	}
	public String getTxtObjeto(){
		return this.txtObjeto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNotificacao: " +  getCdNotificacao();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dsAssunto: " +  getDsAssunto();
		valueToString += ", tpNotificacao: " +  getTpNotificacao();
		valueToString += ", txtNotificacao: " +  getTxtNotificacao();
		valueToString += ", dtNotificacao: " +  sol.util.Util.formatDateTime(getDtNotificacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLeitura: " +  sol.util.Util.formatDateTime(getDtLeitura(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdMensagem: " +  getCdMensagem();
		valueToString += ", cdRegraNotificacao: " +  getCdRegraNotificacao();
		valueToString += ", txtObjeto: " +  getTxtObjeto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Notificacao(getCdNotificacao(),
			getCdUsuario(),
			getDsAssunto(),
			getTpNotificacao(),
			getTxtNotificacao(),
			getDtNotificacao()==null ? null : (GregorianCalendar)getDtNotificacao().clone(),
			getDtLeitura()==null ? null : (GregorianCalendar)getDtLeitura().clone(),
			getCdMensagem(),
			getCdRegraNotificacao(),
			getTxtObjeto());
	}

}