package com.tivic.manager.ptc;

public class TipoDocumentoFase {

	private int cdTipoDocumento;
	private int cdFase;
	private int lgObrigatorio;
	private int nrOrdem;
	private int tpAssociacao;

	public TipoDocumentoFase() { }

	public TipoDocumentoFase(int cdTipoDocumento,
			int cdFase,
			int lgObrigatorio,
			int nrOrdem,
			int tpAssociacao) {
		setCdTipoDocumento(cdTipoDocumento);
		setCdFase(cdFase);
		setLgObrigatorio(lgObrigatorio);
		setNrOrdem(nrOrdem);
		setTpAssociacao(tpAssociacao);
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setCdFase(int cdFase){
		this.cdFase=cdFase;
	}
	public int getCdFase(){
		return this.cdFase;
	}
	public void setLgObrigatorio(int lgObrigatorio){
		this.lgObrigatorio=lgObrigatorio;
	}
	public int getLgObrigatorio(){
		return this.lgObrigatorio;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setTpAssociacao(int tpAssociacao){
		this.tpAssociacao=tpAssociacao;
	}
	public int getTpAssociacao(){
		return this.tpAssociacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", cdFase: " +  getCdFase();
		valueToString += ", lgObrigatorio: " +  getLgObrigatorio();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", tpAssociacao: " +  getTpAssociacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDocumentoFase(getCdTipoDocumento(),
			getCdFase(),
			getLgObrigatorio(),
			getNrOrdem(),
			getTpAssociacao());
	}

}