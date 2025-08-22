package com.tivic.manager.grl;

public class FormularioAtributoOpcao {

	private int cdOpcao;
	private int cdFormularioAtributo;
	private String txtOpcao;
	private float vlReferencia;
	private String idOpcao;
	private int nrOrdem;

	public FormularioAtributoOpcao(){ }
	
	public FormularioAtributoOpcao(int cdOpcao,
			int cdFormularioAtributo,
			String txtOpcao,
			float vlReferencia,
			String idOpcao,
			int nrOrdem){
		setCdOpcao(cdOpcao);
		setCdFormularioAtributo(cdFormularioAtributo);
		setTxtOpcao(txtOpcao);
		setVlReferencia(vlReferencia);
		setIdOpcao(idOpcao);
		setNrOrdem(nrOrdem);
	}
	public void setCdOpcao(int cdOpcao){
		this.cdOpcao=cdOpcao;
	}
	public int getCdOpcao(){
		return this.cdOpcao;
	}
	public void setCdFormularioAtributo(int cdFormularioAtributo){
		this.cdFormularioAtributo=cdFormularioAtributo;
	}
	public int getCdFormularioAtributo(){
		return this.cdFormularioAtributo;
	}
	public void setTxtOpcao(String txtOpcao){
		this.txtOpcao=txtOpcao;
	}
	public String getTxtOpcao(){
		return this.txtOpcao;
	}
	public void setVlReferencia(float vlReferencia){
		this.vlReferencia=vlReferencia;
	}
	public float getVlReferencia(){
		return this.vlReferencia;
	}
	public void setIdOpcao(String idOpcao){
		this.idOpcao=idOpcao;
	}
	public String getIdOpcao(){
		return this.idOpcao;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOpcao: " +  getCdOpcao();
		valueToString += ", cdFormularioAtributo: " +  getCdFormularioAtributo();
		valueToString += ", txtOpcao: " +  getTxtOpcao();
		valueToString += ", vlReferencia: " +  getVlReferencia();
		valueToString += ", idOpcao: " +  getIdOpcao();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormularioAtributoOpcao(getCdOpcao(),
			getCdFormularioAtributo(),
			getTxtOpcao(),
			getVlReferencia(),
			getIdOpcao(),
			getNrOrdem());
	}

}
