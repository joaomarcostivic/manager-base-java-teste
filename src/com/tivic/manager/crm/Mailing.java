package com.tivic.manager.crm;

public class Mailing {

	private int cdMailing;
	private String nmMailing;
	private String txtMailing;
	private int cdModelo;
	private int cdGrupo;
	private int cdContaEnvio;
	private String nmAssunto;

	public Mailing(int cdMailing,
			String nmMailing,
			String txtMailing,
			int cdModelo,
			int cdGrupo,
			int cdContaEnvio,
			String nmAssunto){
		setCdMailing(cdMailing);
		setNmMailing(nmMailing);
		setTxtMailing(txtMailing);
		setCdModelo(cdModelo);
		setCdGrupo(cdGrupo);
		setCdContaEnvio(cdContaEnvio);
		setNmAssunto(nmAssunto);
	}
	public void setCdMailing(int cdMailing){
		this.cdMailing=cdMailing;
	}
	public int getCdMailing(){
		return this.cdMailing;
	}
	public void setNmMailing(String nmMailing){
		this.nmMailing=nmMailing;
	}
	public String getNmMailing(){
		return this.nmMailing;
	}
	public void setTxtMailing(String txtMailing){
		this.txtMailing=txtMailing;
	}
	public String getTxtMailing(){
		return this.txtMailing;
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdContaEnvio(int cdContaEnvio){
		this.cdContaEnvio=cdContaEnvio;
	}
	public int getCdContaEnvio(){
		return this.cdContaEnvio;
	}
	public void setNmAssunto(String nmAssunto){
		this.nmAssunto=nmAssunto;
	}
	public String getNmAssunto(){
		return this.nmAssunto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMailing: " +  getCdMailing();
		valueToString += ", nmMailing: " +  getNmMailing();
		valueToString += ", txtMailing: " +  getTxtMailing();
		valueToString += ", cdModelo: " +  getCdModelo();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", cdContaEnvio: " +  getCdContaEnvio();
		valueToString += ", nmAssunto: " +  getNmAssunto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Mailing(getCdMailing(),
			getNmMailing(),
			getTxtMailing(),
			getCdModelo(),
			getCdGrupo(),
			getCdContaEnvio(),
			getNmAssunto());
	}

}
