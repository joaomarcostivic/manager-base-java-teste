package com.tivic.manager.eCarta;

public class Etiqueta {
	
	private int cdEtiqueta;
	private String sgInicial;
	private long numEtiqueta;
	private String sgFinal;
	private int codLote;
	private int codAit;
	private int tpDocumento;
	
	public int getCdEtiqueta() {
		return cdEtiqueta;
	}
	public void setCdEtiqueta(int cdEtiqueta) {
		this.cdEtiqueta = cdEtiqueta;
	}
	public String getSgInicial() {
		return sgInicial;
	}
	public void setSgInicial(String sgInicial) {
		this.sgInicial = sgInicial;
	}
	public long getNumEtiqueta() {
		return numEtiqueta;
	}
	public void setNumEtiqueta(long numEtiqueta) {
		this.numEtiqueta = numEtiqueta;
	}
	public String getSgFinal() {
		return sgFinal;
	}
	public void setSgFinal(String sgFinal) {
		this.sgFinal = sgFinal;
	}
	public int getCodLote() {
		return codLote;
	}
	public void setCodLote(int codLote) {
		this.codLote = codLote;
	}
	public int getCodAit() {
		return codAit;
	}
	public void setCodAit(int codAit) {
		this.codAit = codAit;
	}
	public int getTpDocumento() {
		return tpDocumento;
	}
	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
	
	public String toString() {
		return "Etiqueta [sgInicial=" + sgInicial + ", numEtiqueta=" + numEtiqueta + ", sgFinal=" + sgFinal
				+ ", codLote=" + codLote + ", codAit=" + codAit + ", tpDocumento=" + tpDocumento + "]";
	}
	

	
}
