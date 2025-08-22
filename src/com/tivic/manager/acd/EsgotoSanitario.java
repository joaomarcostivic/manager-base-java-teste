package com.tivic.manager.acd;

public class EsgotoSanitario {

	private int cdEsgotoSanitario;
	private String nmEsgotoSanitario;
	private String idEsgotoSanitario;

	public EsgotoSanitario(){ }

	public EsgotoSanitario(int cdEsgotoSanitario,
			String nmEsgotoSanitario,
			String idEsgotoSanitario){
		setCdEsgotoSanitario(cdEsgotoSanitario);
		setNmEsgotoSanitario(nmEsgotoSanitario);
		setIdEsgotoSanitario(idEsgotoSanitario);
	}
	public void setCdEsgotoSanitario(int cdEsgotoSanitario){
		this.cdEsgotoSanitario=cdEsgotoSanitario;
	}
	public int getCdEsgotoSanitario(){
		return this.cdEsgotoSanitario;
	}
	public void setNmEsgotoSanitario(String nmEsgotoSanitario){
		this.nmEsgotoSanitario=nmEsgotoSanitario;
	}
	public String getNmEsgotoSanitario(){
		return this.nmEsgotoSanitario;
	}
	public void setIdEsgotoSanitario(String idEsgotoSanitario){
		this.idEsgotoSanitario=idEsgotoSanitario;
	}
	public String getIdEsgotoSanitario(){
		return this.idEsgotoSanitario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEsgotoSanitario: " +  getCdEsgotoSanitario();
		valueToString += ", nmEsgotoSanitario: " +  getNmEsgotoSanitario();
		valueToString += ", idEsgotoSanitario: " +  getIdEsgotoSanitario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EsgotoSanitario(getCdEsgotoSanitario(),
			getNmEsgotoSanitario(),
			getIdEsgotoSanitario());
	}

}