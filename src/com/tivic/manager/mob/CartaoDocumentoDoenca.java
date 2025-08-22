package com.tivic.manager.mob;

public class CartaoDocumentoDoenca {

	private int cdCartaoDocumentoDoenca;
	private int cdDoenca;
	private String txtDescricao;
	private int cdCartaoDocumento;

	public CartaoDocumentoDoenca(){ }

	public CartaoDocumentoDoenca(int cdCartaoDocumentoDoenca,
			int cdDoenca,
			String txtDescricao,
			int cdCartaoDocumento){
		setCdCartaoDocumentoDoenca(cdCartaoDocumentoDoenca);
		setCdDoenca(cdDoenca);
		setTxtDescricao(txtDescricao);
		setCdCartaoDocumento(cdCartaoDocumento);
	}
	public void setCdCartaoDocumentoDoenca(int cdCartaoDocumentoDoenca){
		this.cdCartaoDocumentoDoenca=cdCartaoDocumentoDoenca;
	}
	public int getCdCartaoDocumentoDoenca(){
		return this.cdCartaoDocumentoDoenca;
	}
	public void setCdDoenca(int cdDoenca){
		this.cdDoenca=cdDoenca;
	}
	public int getCdDoenca(){
		return this.cdDoenca;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public void setCdCartaoDocumento(int cdCartaoDocumento){
		this.cdCartaoDocumento=cdCartaoDocumento;
	}
	public int getCdCartaoDocumento(){
		return this.cdCartaoDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCartaoDocumentoDoenca: " +  getCdCartaoDocumentoDoenca();
		valueToString += ", cdDoenca: " +  getCdDoenca();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", cdCartaoDocumento: " +  getCdCartaoDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CartaoDocumentoDoenca(getCdCartaoDocumentoDoenca(),
			getCdDoenca(),
			getTxtDescricao(),
			getCdCartaoDocumento());
	}

}