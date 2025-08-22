package com.tivic.manager.mob;

public class VistoriaAtributoValor {

	private int cdVistoria;
	private int cdFormularioAtributo;
	private String txtValor;
	private byte[] blbValor;
	private int cdOpcao;

	public VistoriaAtributoValor(){ }

	public VistoriaAtributoValor(int cdVistoria,
			int cdFormularioAtributo,
			String txtValor,
			byte[] blbValor,
			int cdOpcao){
		setCdVistoria(cdVistoria);
		setCdFormularioAtributo(cdFormularioAtributo);
		setTxtValor(txtValor);
		setBlbValor(blbValor);
		setCdOpcao(cdOpcao);
	}
	public void setCdVistoria(int cdVistoria){
		this.cdVistoria=cdVistoria;
	}
	public int getCdVistoria(){
		return this.cdVistoria;
	}
	public void setCdFormularioAtributo(int cdFormularioAtributo){
		this.cdFormularioAtributo=cdFormularioAtributo;
	}
	public int getCdFormularioAtributo(){
		return this.cdFormularioAtributo;
	}
	public void setTxtValor(String txtValor){
		this.txtValor=txtValor;
	}
	public String getTxtValor(){
		return this.txtValor;
	}
	public void setBlbValor(byte[] blbValor){
		this.blbValor=blbValor;
	}
	public byte[] getBlbValor(){
		return this.blbValor;
	}
	public void setCdOpcao(int cdOpcao){
		this.cdOpcao=cdOpcao;
	}
	public int getCdOpcao(){
		return this.cdOpcao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVistoria: " +  getCdVistoria();
		valueToString += ", cdFormularioAtributo: " +  getCdFormularioAtributo();
		valueToString += ", txtValor: " +  getTxtValor();
		valueToString += ", blbValor: " +  getBlbValor();
		valueToString += ", cdOpcao: " +  getCdOpcao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VistoriaAtributoValor(getCdVistoria(),
			getCdFormularioAtributo(),
			getTxtValor(),
			getBlbValor(),
			getCdOpcao());
	}

}