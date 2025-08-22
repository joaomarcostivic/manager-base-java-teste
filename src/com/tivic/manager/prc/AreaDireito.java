package com.tivic.manager.prc;

public class AreaDireito {

	private int cdAreaDireito;
	private String nmAreaDireito;
	private String idAreaDireito;
	
	public AreaDireito(){ }

	public AreaDireito(int cdAreaDireito,String nmAreaDireito,String idAreaDireito)	{
		setCdAreaDireito(cdAreaDireito);
		setNmAreaDireito(nmAreaDireito);
		setIdAreaDireito(idAreaDireito);
	}
	public void setCdAreaDireito(int cdAreaDireito){
		this.cdAreaDireito=cdAreaDireito;
	}
	public int getCdAreaDireito(){
		return this.cdAreaDireito;
	}
	public void setNmAreaDireito(String nmAreaDireito){
		this.nmAreaDireito=nmAreaDireito;
	}
	public String getNmAreaDireito(){
		return this.nmAreaDireito;
	}
	public void setIdAreaDireito(String idAreaDireito){
		this.idAreaDireito=idAreaDireito;
	}
	public String getIdAreaDireito(){
		return this.idAreaDireito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAreaDireito: " +  getCdAreaDireito();
		valueToString += ", nmAreaDireito: " +  getNmAreaDireito();
		valueToString += ", idAreaDireito: " +  getIdAreaDireito();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AreaDireito(getCdAreaDireito(),
			getNmAreaDireito(),
			getIdAreaDireito());
	}

}