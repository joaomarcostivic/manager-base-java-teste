package com.tivic.manager.seg;

public class Modulo {

	private int cdModulo;
	private int cdSistema;
	private String nmModulo;
	private String idModulo;
	private String nrVersao;
	private String nrRelease;
	private int lgAtivo;

	public Modulo() { } 
			
	public Modulo(int cdModulo,
			int cdSistema,
			String nmModulo,
			String idModulo,
			String nrVersao,
			String nrRelease,
			int lgAtivo){
		setCdModulo(cdModulo);
		setCdSistema(cdSistema);
		setNmModulo(nmModulo);
		setIdModulo(idModulo);
		setNrVersao(nrVersao);
		setNrRelease(nrRelease);
		setLgAtivo(lgAtivo);
	}
	public void setCdModulo(int cdModulo){
		this.cdModulo=cdModulo;
	}
	public int getCdModulo(){
		return this.cdModulo;
	}
	public void setCdSistema(int cdSistema){
		this.cdSistema=cdSistema;
	}
	public int getCdSistema(){
		return this.cdSistema;
	}
	public void setNmModulo(String nmModulo){
		this.nmModulo=nmModulo;
	}
	public String getNmModulo(){
		return this.nmModulo;
	}
	public void setIdModulo(String idModulo){
		this.idModulo=idModulo;
	}
	public String getIdModulo(){
		return this.idModulo;
	}
	public void setNrVersao(String nrVersao){
		this.nrVersao=nrVersao;
	}
	public String getNrVersao(){
		return this.nrVersao;
	}
	public void setNrRelease(String nrRelease){
		this.nrRelease=nrRelease;
	}
	public String getNrRelease(){
		return this.nrRelease;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModulo: " +  getCdModulo();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", nmModulo: " +  getNmModulo();
		valueToString += ", idModulo: " +  getIdModulo();
		valueToString += ", nrVersao: " +  getNrVersao();
		valueToString += ", nrRelease: " +  getNrRelease();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Modulo(getCdModulo(),
			getCdSistema(),
			getNmModulo(),
			getIdModulo(),
			getNrVersao(),
			getNrRelease(),
			getLgAtivo());
	}

}
