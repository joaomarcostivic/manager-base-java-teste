package com.tivic.manager.crm;

public class FormaContato {

	private int cdFormaContato;
	private String nmFormaContato;

	public FormaContato(int cdFormaContato,
			String nmFormaContato){
		setCdFormaContato(cdFormaContato);
		setNmFormaContato(nmFormaContato);
	}
	public void setCdFormaContato(int cdFormaContato){
		this.cdFormaContato=cdFormaContato;
	}
	public int getCdFormaContato(){
		return this.cdFormaContato;
	}
	public void setNmFormaContato(String nmFormaContato){
		this.nmFormaContato=nmFormaContato;
	}
	public String getNmFormaContato(){
		return this.nmFormaContato;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormaContato: " +  getCdFormaContato();
		valueToString += ", nmFormaContato: " +  getNmFormaContato();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormaContato(getCdFormaContato(),
			getNmFormaContato());
	}

}
