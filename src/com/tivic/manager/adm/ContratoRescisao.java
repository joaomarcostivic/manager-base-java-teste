package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContratoRescisao {

	private int cdContrato;
	private int cdDocumento;
	private GregorianCalendar dtRescisao;
	private int tpFormaContato;
	private int cdNegociacaoRescisao;

	public ContratoRescisao(int cdContrato,
			int cdDocumento,
			GregorianCalendar dtRescisao,
			int tpFormaContato,
			int cdNegociacaoRescisao){
		setCdContrato(cdContrato);
		setCdDocumento(cdDocumento);
		setDtRescisao(dtRescisao);
		setTpFormaContato(tpFormaContato);
		setCdNegociacaoRescisao(cdNegociacaoRescisao);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setDtRescisao(GregorianCalendar dtRescisao){
		this.dtRescisao=dtRescisao;
	}
	public GregorianCalendar getDtRescisao(){
		return this.dtRescisao;
	}
	public void setTpFormaContato(int tpFormaContato){
		this.tpFormaContato=tpFormaContato;
	}
	public int getTpFormaContato(){
		return this.tpFormaContato;
	}
	public void setCdNegociacaoRescisao(int cdNegociacaoRescisao){
		this.cdNegociacaoRescisao=cdNegociacaoRescisao;
	}
	public int getCdNegociacaoRescisao(){
		return this.cdNegociacaoRescisao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", dtRescisao: " +  sol.util.Util.formatDateTime(getDtRescisao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpFormaContato: " +  getTpFormaContato();
		valueToString += ", cdNegociacaoRescisao: " +  getCdNegociacaoRescisao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoRescisao(getCdContrato(),
			getCdDocumento(),
			getDtRescisao()==null ? null : (GregorianCalendar)getDtRescisao().clone(),
			getTpFormaContato(),
			getCdNegociacaoRescisao());
	}

}
