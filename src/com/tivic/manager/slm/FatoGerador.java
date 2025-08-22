package com.tivic.manager.slm;

public class FatoGerador {

	private int cdFatoGerador;
	private String nmFatoGerador;
	private String txtFatoGerador;
	private String idFatoGerador;
	private int cdFatoGeradorSuperior;
	private int tpOrigem;
	private int tpFatoGerador;
	private int tpCausa;

	public FatoGerador() { }

	public FatoGerador(int cdFatoGerador,
			String nmFatoGerador,
			String txtFatoGerador,
			String idFatoGerador,
			int cdFatoGeradorSuperior,
			int tpOrigem,
			int tpFatoGerador,
			int tpCausa) {
		setCdFatoGerador(cdFatoGerador);
		setNmFatoGerador(nmFatoGerador);
		setTxtFatoGerador(txtFatoGerador);
		setIdFatoGerador(idFatoGerador);
		setCdFatoGeradorSuperior(cdFatoGeradorSuperior);
		setTpOrigem(tpOrigem);
		setTpFatoGerador(tpFatoGerador);
		setTpCausa(tpCausa);
	}
	public void setCdFatoGerador(int cdFatoGerador){
		this.cdFatoGerador=cdFatoGerador;
	}
	public int getCdFatoGerador(){
		return this.cdFatoGerador;
	}
	public void setNmFatoGerador(String nmFatoGerador){
		this.nmFatoGerador=nmFatoGerador;
	}
	public String getNmFatoGerador(){
		return this.nmFatoGerador;
	}
	public void setTxtFatoGerador(String txtFatoGerador){
		this.txtFatoGerador=txtFatoGerador;
	}
	public String getTxtFatoGerador(){
		return this.txtFatoGerador;
	}
	public void setIdFatoGerador(String idFatoGerador){
		this.idFatoGerador=idFatoGerador;
	}
	public String getIdFatoGerador(){
		return this.idFatoGerador;
	}
	public void setCdFatoGeradorSuperior(int cdFatoGeradorSuperior){
		this.cdFatoGeradorSuperior=cdFatoGeradorSuperior;
	}
	public int getCdFatoGeradorSuperior(){
		return this.cdFatoGeradorSuperior;
	}
	public void setTpOrigem(int tpOrigem){
		this.tpOrigem=tpOrigem;
	}
	public int getTpOrigem(){
		return this.tpOrigem;
	}
	public void setTpFatoGerador(int tpFatoGerador){
		this.tpFatoGerador=tpFatoGerador;
	}
	public int getTpFatoGerador(){
		return this.tpFatoGerador;
	}
	public void setTpCausa(int tpCausa){
		this.tpCausa=tpCausa;
	}
	public int getTpCausa(){
		return this.tpCausa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFatoGerador: " +  getCdFatoGerador();
		valueToString += ", nmFatoGerador: " +  getNmFatoGerador();
		valueToString += ", txtFatoGerador: " +  getTxtFatoGerador();
		valueToString += ", idFatoGerador: " +  getIdFatoGerador();
		valueToString += ", cdFatoGeradorSuperior: " +  getCdFatoGeradorSuperior();
		valueToString += ", tpOrigem: " +  getTpOrigem();
		valueToString += ", tpFatoGerador: " +  getTpFatoGerador();
		valueToString += ", tpCausa: " +  getTpCausa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FatoGerador(getCdFatoGerador(),
			getNmFatoGerador(),
			getTxtFatoGerador(),
			getIdFatoGerador(),
			getCdFatoGeradorSuperior(),
			getTpOrigem(),
			getTpFatoGerador(),
			getTpCausa());
	}

}