package com.tivic.manager.hlp;

public class Imagem {

	private int cdImagem;
	private String idImagem;
	private byte[] blbImagem;
	private String nmTitulo;
	private String txtRotulo;

	public Imagem(int cdImagem,
			String idImagem,
			byte[] blbImagem,
			String nmTitulo,
			String txtRotulo){
		setCdImagem(cdImagem);
		setIdImagem(idImagem);
		setBlbImagem(blbImagem);
		setNmTitulo(nmTitulo);
		setTxtRotulo(txtRotulo);
	}
	public void setCdImagem(int cdImagem){
		this.cdImagem=cdImagem;
	}
	public int getCdImagem(){
		return this.cdImagem;
	}
	public void setIdImagem(String idImagem){
		this.idImagem=idImagem;
	}
	public String getIdImagem(){
		return this.idImagem;
	}
	public void setBlbImagem(byte[] blbImagem){
		this.blbImagem=blbImagem;
	}
	public byte[] getBlbImagem(){
		return this.blbImagem;
	}
	public void setNmTitulo(String nmTitulo){
		this.nmTitulo=nmTitulo;
	}
	public String getNmTitulo(){
		return this.nmTitulo;
	}
	public void setTxtRotulo(String txtRotulo){
		this.txtRotulo=txtRotulo;
	}
	public String getTxtRotulo(){
		return this.txtRotulo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdImagem: " +  getCdImagem();
		valueToString += ", idImagem: " +  getIdImagem();
		valueToString += ", blbImagem: " +  getBlbImagem();
		valueToString += ", nmTitulo: " +  getNmTitulo();
		valueToString += ", txtRotulo: " +  getTxtRotulo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Imagem(getCdImagem(),
			getIdImagem(),
			getBlbImagem(),
			getNmTitulo(),
			getTxtRotulo());
	}

}
