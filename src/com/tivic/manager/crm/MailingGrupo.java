package com.tivic.manager.crm;

public class MailingGrupo {

	private int cdGrupo;
	private String nmGrupo;
	private String txtGrupo;

	public MailingGrupo(int cdGrupo,
			String nmGrupo,
			String txtGrupo){
		setCdGrupo(cdGrupo);
		setNmGrupo(nmGrupo);
		setTxtGrupo(txtGrupo);
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setNmGrupo(String nmGrupo){
		this.nmGrupo=nmGrupo;
	}
	public String getNmGrupo(){
		return this.nmGrupo;
	}
	public void setTxtGrupo(String txtGrupo){
		this.txtGrupo=txtGrupo;
	}
	public String getTxtGrupo(){
		return this.txtGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		valueToString += ", txtGrupo: " +  getTxtGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MailingGrupo(getCdGrupo(),
			getNmGrupo(),
			getTxtGrupo());
	}

}
