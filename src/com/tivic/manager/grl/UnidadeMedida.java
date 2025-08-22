package com.tivic.manager.grl;

public class UnidadeMedida {

	private int cdUnidadeMedida;
	private String nmUnidadeMedida;
	private String sgUnidadeMedida;
	private String txtUnidadeMedida;
	private int nrPrecisaoMedida;
	private int lgAtivo;

	public UnidadeMedida(int cdUnidadeMedida,
			String nmUnidadeMedida,
			String sgUnidadeMedida,
			String txtUnidadeMedida,
			int nrPrecisaoMedida,
			int lgAtivo){
		setCdUnidadeMedida(cdUnidadeMedida);
		setNmUnidadeMedida(nmUnidadeMedida);
		setSgUnidadeMedida(sgUnidadeMedida);
		setTxtUnidadeMedida(txtUnidadeMedida);
		setNrPrecisaoMedida(nrPrecisaoMedida);
		setLgAtivo(lgAtivo);
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public void setNmUnidadeMedida(String nmUnidadeMedida){
		this.nmUnidadeMedida=nmUnidadeMedida;
	}
	public String getNmUnidadeMedida(){
		return this.nmUnidadeMedida;
	}
	public void setSgUnidadeMedida(String sgUnidadeMedida){
		this.sgUnidadeMedida=sgUnidadeMedida;
	}
	public String getSgUnidadeMedida(){
		return this.sgUnidadeMedida;
	}
	public void setTxtUnidadeMedida(String txtUnidadeMedida){
		this.txtUnidadeMedida=txtUnidadeMedida;
	}
	public String getTxtUnidadeMedida(){
		return this.txtUnidadeMedida;
	}
	public void setNrPrecisaoMedida(int nrPrecisaoMedida){
		this.nrPrecisaoMedida=nrPrecisaoMedida;
	}
	public int getNrPrecisaoMedida(){
		return this.nrPrecisaoMedida;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdUnidadeMedida: " +  getCdUnidadeMedida();
		valueToString += ", nmUnidadeMedida: " +  getNmUnidadeMedida();
		valueToString += ", sgUnidadeMedida: " +  getSgUnidadeMedida();
		valueToString += ", txtUnidadeMedida: " +  getTxtUnidadeMedida();
		valueToString += ", nrPrecisaoMedida: " +  getNrPrecisaoMedida();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UnidadeMedida(getCdUnidadeMedida(),
			getNmUnidadeMedida(),
			getSgUnidadeMedida(),
			getTxtUnidadeMedida(),
			getNrPrecisaoMedida(),
			getLgAtivo());
	}

}
