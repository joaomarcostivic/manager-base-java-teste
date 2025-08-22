package com.tivic.manager.msg;

public class MensagemArquivo {

	private int cdMensagem;
	private String nmArquivo;
	private byte[] blbArquivo;

	public MensagemArquivo(){ }

	public MensagemArquivo(int cdMensagem,
			String nmArquivo,
			byte[] blbArquivo){
		setCdMensagem(cdMensagem);
		setNmArquivo(nmArquivo);
		setBlbArquivo(blbArquivo);
	}
	public void setCdMensagem(int cdMensagem){
		this.cdMensagem=cdMensagem;
	}
	public int getCdMensagem(){
		return this.cdMensagem;
	}
	public void setNmArquivo(String nmArquivo){
		this.nmArquivo=nmArquivo;
	}
	public String getNmArquivo(){
		return this.nmArquivo;
	}
	public void setBlbArquivo(byte[] blbArquivo){
		this.blbArquivo=blbArquivo;
	}
	public byte[] getBlbArquivo(){
		return this.blbArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMensagem: " +  getCdMensagem();
		valueToString += ", nmArquivo: " +  getNmArquivo();
		valueToString += ", blbArquivo: " +  getBlbArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MensagemArquivo(getCdMensagem(),
			getNmArquivo(),
			getBlbArquivo());
	}

}
