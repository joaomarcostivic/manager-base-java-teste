package com.tivic.manager.crm;

public class MailingConta {

	private int cdConta;
	private String nmConta;
	private String nmEmail;
	private String nmServidorPop;
	private String nmServidorSmtp;
	private String dsAssinatura;
	private int nrPortaPop;
	private int nrPortaSmtp;
	private int lgAutenticacaoPop;
	private int lgAutenticacaoSmtp;
	private String nmLogin;
	private String nmSenha;
	private int lgSslPop;
	private int lgSslSmtp;

	public MailingConta(int cdConta,
			String nmConta,
			String nmEmail,
			String nmServidorPop,
			String nmServidorSmtp,
			String dsAssinatura,
			int nrPortaPop,
			int nrPortaSmtp,
			int lgAutenticacaoPop,
			int lgAutenticacaoSmtp,
			String nmLogin,
			String nmSenha,
			int lgSslPop,
			int lgSslSmtp){
		setCdConta(cdConta);
		setNmConta(nmConta);
		setNmEmail(nmEmail);
		setNmServidorPop(nmServidorPop);
		setNmServidorSmtp(nmServidorSmtp);
		setDsAssinatura(dsAssinatura);
		setNrPortaPop(nrPortaPop);
		setNrPortaSmtp(nrPortaSmtp);
		setLgAutenticacaoPop(lgAutenticacaoPop);
		setLgAutenticacaoSmtp(lgAutenticacaoSmtp);
		setNmLogin(nmLogin);
		setNmSenha(nmSenha);
		setLgSslPop(lgSslPop);
		setLgSslSmtp(lgSslSmtp);
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setNmConta(String nmConta){
		this.nmConta=nmConta;
	}
	public String getNmConta(){
		return this.nmConta;
	}
	public void setNmEmail(String nmEmail){
		this.nmEmail=nmEmail;
	}
	public String getNmEmail(){
		return this.nmEmail;
	}
	public void setNmServidorPop(String nmServidorPop){
		this.nmServidorPop=nmServidorPop;
	}
	public String getNmServidorPop(){
		return this.nmServidorPop;
	}
	public void setNmServidorSmtp(String nmServidorSmtp){
		this.nmServidorSmtp=nmServidorSmtp;
	}
	public String getNmServidorSmtp(){
		return this.nmServidorSmtp;
	}
	public void setDsAssinatura(String dsAssinatura){
		this.dsAssinatura=dsAssinatura;
	}
	public String getDsAssinatura(){
		return this.dsAssinatura;
	}
	public void setNrPortaPop(int nrPortaPop){
		this.nrPortaPop=nrPortaPop;
	}
	public int getNrPortaPop(){
		return this.nrPortaPop;
	}
	public void setNrPortaSmtp(int nrPortaSmtp){
		this.nrPortaSmtp=nrPortaSmtp;
	}
	public int getNrPortaSmtp(){
		return this.nrPortaSmtp;
	}
	public void setLgAutenticacaoPop(int lgAutenticacaoPop){
		this.lgAutenticacaoPop=lgAutenticacaoPop;
	}
	public int getLgAutenticacaoPop(){
		return this.lgAutenticacaoPop;
	}
	public void setLgAutenticacaoSmtp(int lgAutenticacaoSmtp){
		this.lgAutenticacaoSmtp=lgAutenticacaoSmtp;
	}
	public int getLgAutenticacaoSmtp(){
		return this.lgAutenticacaoSmtp;
	}
	public void setNmLogin(String nmLogin){
		this.nmLogin=nmLogin;
	}
	public String getNmLogin(){
		return this.nmLogin;
	}
	public void setNmSenha(String nmSenha){
		this.nmSenha=nmSenha;
	}
	public String getNmSenha(){
		return this.nmSenha;
	}
	public void setLgSslPop(int lgSslPop){
		this.lgSslPop=lgSslPop;
	}
	public int getLgSslPop(){
		return this.lgSslPop;
	}
	public void setLgSslSmtp(int lgSslSmtp){
		this.lgSslSmtp=lgSslSmtp;
	}
	public int getLgSslSmtp(){
		return this.lgSslSmtp;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", nmConta: " +  getNmConta();
		valueToString += ", nmEmail: " +  getNmEmail();
		valueToString += ", nmServidorPop: " +  getNmServidorPop();
		valueToString += ", nmServidorSmtp: " +  getNmServidorSmtp();
		valueToString += ", dsAssinatura: " +  getDsAssinatura();
		valueToString += ", nrPortaPop: " +  getNrPortaPop();
		valueToString += ", nrPortaSmtp: " +  getNrPortaSmtp();
		valueToString += ", lgAutenticacaoPop: " +  getLgAutenticacaoPop();
		valueToString += ", lgAutenticacaoSmtp: " +  getLgAutenticacaoSmtp();
		valueToString += ", nmLogin: " +  getNmLogin();
		valueToString += ", nmSenha: " +  getNmSenha();
		valueToString += ", lgSslPop: " +  getLgSslPop();
		valueToString += ", lgSslSmtp: " +  getLgSslSmtp();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MailingConta(getCdConta(),
			getNmConta(),
			getNmEmail(),
			getNmServidorPop(),
			getNmServidorSmtp(),
			getDsAssinatura(),
			getNrPortaPop(),
			getNrPortaSmtp(),
			getLgAutenticacaoPop(),
			getLgAutenticacaoSmtp(),
			getNmLogin(),
			getNmSenha(),
			getLgSslPop(),
			getLgSslSmtp());
	}

}
