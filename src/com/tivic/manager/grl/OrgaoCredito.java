package com.tivic.manager.grl;

public class OrgaoCredito {

	private int cdOrgao;
	private String nmOrgao;
	private int lgOficial;
	private String nrTelefone;
	private String nrTelefone2;
	private String nrFax;
	private String nmEmail;
	private String nmUrl;

	public OrgaoCredito(int cdOrgao,
			String nmOrgao,
			int lgOficial,
			String nrTelefone,
			String nrTelefone2,
			String nrFax,
			String nmEmail,
			String nmUrl){
		setCdOrgao(cdOrgao);
		setNmOrgao(nmOrgao);
		setLgOficial(lgOficial);
		setNrTelefone(nrTelefone);
		setNrTelefone2(nrTelefone2);
		setNrFax(nrFax);
		setNmEmail(nmEmail);
		setNmUrl(nmUrl);
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setNmOrgao(String nmOrgao){
		this.nmOrgao=nmOrgao;
	}
	public String getNmOrgao(){
		return this.nmOrgao;
	}
	public void setLgOficial(int lgOficial){
		this.lgOficial=lgOficial;
	}
	public int getLgOficial(){
		return this.lgOficial;
	}
	public void setNrTelefone(String nrTelefone){
		this.nrTelefone=nrTelefone;
	}
	public String getNrTelefone(){
		return this.nrTelefone;
	}
	public void setNrTelefone2(String nrTelefone2){
		this.nrTelefone2=nrTelefone2;
	}
	public String getNrTelefone2(){
		return this.nrTelefone2;
	}
	public void setNrFax(String nrFax){
		this.nrFax=nrFax;
	}
	public String getNrFax(){
		return this.nrFax;
	}
	public void setNmEmail(String nmEmail){
		this.nmEmail=nmEmail;
	}
	public String getNmEmail(){
		return this.nmEmail;
	}
	public void setNmUrl(String nmUrl){
		this.nmUrl=nmUrl;
	}
	public String getNmUrl(){
		return this.nmUrl;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrgao: " +  getCdOrgao();
		valueToString += ", nmOrgao: " +  getNmOrgao();
		valueToString += ", lgOficial: " +  getLgOficial();
		valueToString += ", nrTelefone: " +  getNrTelefone();
		valueToString += ", nrTelefone2: " +  getNrTelefone2();
		valueToString += ", nrFax: " +  getNrFax();
		valueToString += ", nmEmail: " +  getNmEmail();
		valueToString += ", nmUrl: " +  getNmUrl();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrgaoCredito(cdOrgao,
			nmOrgao,
			lgOficial,
			nrTelefone,
			nrTelefone2,
			nrFax,
			nmEmail,
			nmUrl);
	}

}