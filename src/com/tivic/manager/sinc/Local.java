package com.tivic.manager.sinc;

public class Local {

	private int cdLocal;
	private String idLocal;
	private int tpLocal;
	private String nmLoginDatabase;
	private String nmSenhaDatabase;
	private String nmUrlDatabase;

	public Local(){ }

	public Local(int cdLocal,
			String idLocal,
			int tpLocal,
			String nmLoginDatabase,
			String nmSenhaDatabase,
			String nmUrlDatabase){
		setCdLocal(cdLocal);
		setIdLocal(idLocal);
		setTpLocal(tpLocal);
		setNmLoginDatabase(nmLoginDatabase);
		setNmSenhaDatabase(nmSenhaDatabase);
		setNmUrlDatabase(nmUrlDatabase);
	}
	public void setCdLocal(int cdLocal){
		this.cdLocal=cdLocal;
	}
	public int getCdLocal(){
		return this.cdLocal;
	}
	public void setIdLocal(String idLocal){
		this.idLocal=idLocal;
	}
	public String getIdLocal(){
		return this.idLocal;
	}
	public void setTpLocal(int tpLocal){
		this.tpLocal=tpLocal;
	}
	public int getTpLocal(){
		return this.tpLocal;
	}
	public void setNmLoginDatabase(String nmLoginDatabase){
		this.nmLoginDatabase=nmLoginDatabase;
	}
	public String getNmLoginDatabase(){
		return this.nmLoginDatabase;
	}
	public void setNmSenhaDatabase(String nmSenhaDatabase){
		this.nmSenhaDatabase=nmSenhaDatabase;
	}
	public String getNmSenhaDatabase(){
		return this.nmSenhaDatabase;
	}
	public void setNmUrlDatabase(String nmUrlDatabase){
		this.nmUrlDatabase=nmUrlDatabase;
	}
	public String getNmUrlDatabase(){
		return this.nmUrlDatabase;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLocal: " +  getCdLocal();
		valueToString += ", idLocal: " +  getIdLocal();
		valueToString += ", tpLocal: " +  getTpLocal();
		valueToString += ", nmLoginDatabase: " +  getNmLoginDatabase();
		valueToString += ", nmSenhaDatabase: " +  getNmSenhaDatabase();
		valueToString += ", nmUrlDatabase: " +  getNmUrlDatabase();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Local(getCdLocal(),
			getIdLocal(),
			getTpLocal(),
			getNmLoginDatabase(),
			getNmSenhaDatabase(),
			getNmUrlDatabase());
	}

}