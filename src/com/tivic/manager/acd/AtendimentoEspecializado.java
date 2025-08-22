package com.tivic.manager.acd;

public class AtendimentoEspecializado {

	private int cdAtendimentoEspecializado;
	private String nmAtendimentoEspecializado;
	private String idAtendimentoEspecializado;

	public AtendimentoEspecializado() { }
			
	public AtendimentoEspecializado(int cdAtendimentoEspecializado,
			String nmAtendimentoEspecializado,
			String idAtendimentoEspecializado){
		setCdAtendimentoEspecializado(cdAtendimentoEspecializado);
		setNmAtendimentoEspecializado(nmAtendimentoEspecializado);
		setIdAtendimentoEspecializado(idAtendimentoEspecializado);
	}
	public void setCdAtendimentoEspecializado(int cdAtendimentoEspecializado){
		this.cdAtendimentoEspecializado=cdAtendimentoEspecializado;
	}
	public int getCdAtendimentoEspecializado(){
		return this.cdAtendimentoEspecializado;
	}
	public void setNmAtendimentoEspecializado(String nmAtendimentoEspecializado){
		this.nmAtendimentoEspecializado=nmAtendimentoEspecializado;
	}
	public String getNmAtendimentoEspecializado(){
		return this.nmAtendimentoEspecializado;
	}
	public void setIdAtendimentoEspecializado(String idAtendimentoEspecializado){
		this.idAtendimentoEspecializado=idAtendimentoEspecializado;
	}
	public String getIdAtendimentoEspecializado(){
		return this.idAtendimentoEspecializado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtendimentoEspecializado: " +  getCdAtendimentoEspecializado();
		valueToString += ", nmAtendimentoEspecializado: " +  getNmAtendimentoEspecializado();
		valueToString += ", idAtendimentoEspecializado: " +  getIdAtendimentoEspecializado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AtendimentoEspecializado(getCdAtendimentoEspecializado(),
			getNmAtendimentoEspecializado(),
			getIdAtendimentoEspecializado());
	}

}