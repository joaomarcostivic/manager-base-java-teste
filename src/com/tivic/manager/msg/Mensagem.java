package com.tivic.manager.msg;

import java.util.GregorianCalendar;

public class Mensagem {

	private int cdMensagem;
	private String dsAssunto;
	private String txtMensagem;
	private int cdUsuarioOrigem;
	private GregorianCalendar dtEnvio;
	private int lgConfirmacao;
	private int lgImportante;
	private int lgCopiaEmail;
	private int cdMensagemOrigem;
	private int cdProcesso;
	private int cdAgendaItem;
	private int stMensagemRemetente;

	public Mensagem(){ }

	public Mensagem(int cdMensagem,
			String dsAssunto,
			String txtMensagem,
			int cdUsuarioOrigem,
			GregorianCalendar dtEnvio,
			int lgConfirmacao,
			int lgImportante,
			int lgCopiaEmail,
			int cdMensagemOrigem,
			int cdProcesso,
			int cdAgendaItem,
			int stMensagemRemetente){
		setCdMensagem(cdMensagem);
		setDsAssunto(dsAssunto);
		setTxtMensagem(txtMensagem);
		setCdUsuarioOrigem(cdUsuarioOrigem);
		setDtEnvio(dtEnvio);
		setLgConfirmacao(lgConfirmacao);
		setLgImportante(lgImportante);
		setLgCopiaEmail(lgCopiaEmail);
		setCdMensagemOrigem(cdMensagemOrigem);
		setCdProcesso(cdProcesso);
		setCdAgendaItem(cdAgendaItem);
		setStMensagemRemetente(stMensagemRemetente);
	}
	public void setCdMensagem(int cdMensagem){
		this.cdMensagem=cdMensagem;
	}
	public int getCdMensagem(){
		return this.cdMensagem;
	}
	public void setDsAssunto(String dsAssunto){
		this.dsAssunto=dsAssunto;
	}
	public String getDsAssunto(){
		return this.dsAssunto;
	}
	public void setTxtMensagem(String txtMensagem){
		this.txtMensagem=txtMensagem;
	}
	public String getTxtMensagem(){
		return this.txtMensagem;
	}
	public void setCdUsuarioOrigem(int cdUsuarioOrigem){
		this.cdUsuarioOrigem=cdUsuarioOrigem;
	}
	public int getCdUsuarioOrigem(){
		return this.cdUsuarioOrigem;
	}
	public void setDtEnvio(GregorianCalendar dtEnvio){
		this.dtEnvio=dtEnvio;
	}
	public GregorianCalendar getDtEnvio(){
		return this.dtEnvio;
	}
	public void setLgConfirmacao(int lgConfirmacao){
		this.lgConfirmacao=lgConfirmacao;
	}
	public int getLgConfirmacao(){
		return this.lgConfirmacao;
	}
	public void setLgImportante(int lgImportante){
		this.lgImportante=lgImportante;
	}
	public int getLgImportante(){
		return this.lgImportante;
	}
	public void setLgCopiaEmail(int lgCopiaEmail){
		this.lgCopiaEmail=lgCopiaEmail;
	}
	public int getLgCopiaEmail(){
		return this.lgCopiaEmail;
	}
	public void setCdMensagemOrigem(int cdMensagemOrigem){
		this.cdMensagemOrigem=cdMensagemOrigem;
	}
	public int getCdMensagemOrigem(){
		return this.cdMensagemOrigem;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdAgendaItem(int cdAgendaItem){
		this.cdAgendaItem=cdAgendaItem;
	}
	public int getCdAgendaItem(){
		return this.cdAgendaItem;
	}
	public int getStMensagemRemetente() {
		return stMensagemRemetente;
	}

	public void setStMensagemRemetente(int stMensagemRemetente) {
		this.stMensagemRemetente = stMensagemRemetente;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdMensagem: " +  getCdMensagem();
		valueToString += ", dsAssunto: " +  getDsAssunto();
		valueToString += ", txtMensagem: " +  getTxtMensagem();
		valueToString += ", cdUsuarioOrigem: " +  getCdUsuarioOrigem();
		valueToString += ", dtEnvio: " +  sol.util.Util.formatDateTime(getDtEnvio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgConfirmacao: " +  getLgConfirmacao();
		valueToString += ", lgImportante: " +  getLgImportante();
		valueToString += ", lgCopiaEmail: " +  getLgCopiaEmail();
		valueToString += ", cdMensagemOrigem: " +  getCdMensagemOrigem();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdAgendaItem: " +  getCdAgendaItem();
		valueToString += ", stMensagemRemetente: " +  getStMensagemRemetente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Mensagem(getCdMensagem(),
			getDsAssunto(),
			getTxtMensagem(),
			getCdUsuarioOrigem(),
			getDtEnvio()==null ? null : (GregorianCalendar)getDtEnvio().clone(),
			getLgConfirmacao(),
			getLgImportante(),
			getLgCopiaEmail(),
			getCdMensagemOrigem(),
			getCdProcesso(),
			getCdAgendaItem(),
			getStMensagemRemetente());
	}

}