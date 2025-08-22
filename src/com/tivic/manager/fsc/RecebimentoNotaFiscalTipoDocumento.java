package com.tivic.manager.fsc;

public class RecebimentoNotaFiscalTipoDocumento {

	private int cdRecebimentoNotaFiscal;
	private int cdTipoDocumento;
	private int lgPresente;

	public RecebimentoNotaFiscalTipoDocumento() { }

	public RecebimentoNotaFiscalTipoDocumento(int cdRecebimentoNotaFiscal,
												int cdTipoDocumento,
												int lgPresente) {
		setCdRecebimentoNotaFiscal(cdRecebimentoNotaFiscal);
		setCdTipoDocumento(cdTipoDocumento);
		setLgPresente(lgPresente);
	}
	public void setCdRecebimentoNotaFiscal(int cdRecebimentoNotaFiscal){
		this.cdRecebimentoNotaFiscal=cdRecebimentoNotaFiscal;
	}
	public int getCdRecebimentoNotaFiscal(){
		return this.cdRecebimentoNotaFiscal;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setLgPresente(int lgPresente) {
		this.lgPresente = lgPresente;
	}
	public int getLgPresente() {
		return lgPresente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRecebimentoNotaFiscal: " +  getCdRecebimentoNotaFiscal();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", lgPresente: " +  getLgPresente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RecebimentoNotaFiscalTipoDocumento(getCdRecebimentoNotaFiscal(),
			getCdTipoDocumento(),
			getLgPresente());
	}

}