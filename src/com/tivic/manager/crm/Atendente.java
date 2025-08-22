package com.tivic.manager.crm;

public class Atendente {

	private int cdCentral;
	private int cdUsuario;
	private String nmLoginIm;
	private String nmSenhaIm;
	private String nmApelidoIm;

	public Atendente(int cdCentral,
			int cdUsuario,
			String nmLoginIm,
			String nmSenhaIm,
			String nmApelidoIm){
		setCdCentral(cdCentral);
		setCdUsuario(cdUsuario);
		setNmLoginIm(nmLoginIm);
		setNmSenhaIm(nmSenhaIm);
		setNmApelidoIm(nmApelidoIm);
	}
	public void setCdCentral(int cdCentral){
		this.cdCentral=cdCentral;
	}
	public int getCdCentral(){
		return this.cdCentral;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setNmLoginIm(String nmLoginIm){
		this.nmLoginIm=nmLoginIm;
	}
	public String getNmLoginIm(){
		return this.nmLoginIm;
	}
	public void setNmSenhaIm(String nmSenhaIm){
		this.nmSenhaIm=nmSenhaIm;
	}
	public String getNmSenhaIm(){
		return this.nmSenhaIm;
	}
	public void setNmApelidoIm(String nmApelidoIm){
		this.nmApelidoIm=nmApelidoIm;
	}
	public String getNmApelidoIm(){
		return this.nmApelidoIm;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCentral: " +  getCdCentral();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", nmLoginIm: " +  getNmLoginIm();
		valueToString += ", nmSenhaIm: " +  getNmSenhaIm();
		valueToString += ", nmApelidoIm: " +  getNmApelidoIm();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Atendente(getCdCentral(),
			getCdUsuario(),
			getNmLoginIm(),
			getNmSenhaIm(),
			getNmApelidoIm());
	}

}
