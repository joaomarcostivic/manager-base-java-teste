package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class DocumentoEntradaOcorrencia {

	private int cdOcorrencia;
	private int cdDocumentoEntrada;
	private int cdTipoOcorrencia;
	private GregorianCalendar dtOcorrencia;
	private String txtOcorrencia;
	private int stOcorrencia;

	public DocumentoEntradaOcorrencia() { }

	public DocumentoEntradaOcorrencia(int cdOcorrencia,
			int cdDocumentoEntrada,
			int cdTipoOcorrencia,
			GregorianCalendar dtOcorrencia,
			String txtOcorrencia,
			int stOcorrencia) {
		setCdOcorrencia(cdOcorrencia);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setDtOcorrencia(dtOcorrencia);
		setTxtOcorrencia(txtOcorrencia);
		setStOcorrencia(stOcorrencia);
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdTipoOcorrencia(int cdTipoOcorrencia){
		this.cdTipoOcorrencia=cdTipoOcorrencia;
	}
	public int getCdTipoOcorrencia(){
		return this.cdTipoOcorrencia;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setTxtOcorrencia(String txtOcorrencia){
		this.txtOcorrencia=txtOcorrencia;
	}
	public String getTxtOcorrencia(){
		return this.txtOcorrencia;
	}
	public void setStOcorrencia(int stOcorrencia){
		this.stOcorrencia=stOcorrencia;
	}
	public int getStOcorrencia(){
		return this.stOcorrencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtOcorrencia: " +  getTxtOcorrencia();
		valueToString += ", stOcorrencia: " +  getStOcorrencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoEntradaOcorrencia(getCdOcorrencia(),
			getCdDocumentoEntrada(),
			getCdTipoOcorrencia(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getTxtOcorrencia(),
			getStOcorrencia());
	}

}