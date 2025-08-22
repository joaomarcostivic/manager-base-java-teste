package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class FaixaDesconto {

	private int cdTipoDesconto;
	private int cdEmpresa;
	private int cdFaixaDesconto;
	private float prDesconto;
	private GregorianCalendar dtInicialValidade;
	private GregorianCalendar dtFinalValidade;

	public FaixaDesconto(int cdTipoDesconto,
			int cdEmpresa,
			int cdFaixaDesconto,
			float prDesconto,
			GregorianCalendar dtInicialValidade,
			GregorianCalendar dtFinalValidade){
		setCdTipoDesconto(cdTipoDesconto);
		setCdEmpresa(cdEmpresa);
		setCdFaixaDesconto(cdFaixaDesconto);
		setPrDesconto(prDesconto);
		setDtInicialValidade(dtInicialValidade);
		setDtFinalValidade(dtFinalValidade);
	}
	public void setCdTipoDesconto(int cdTipoDesconto){
		this.cdTipoDesconto=cdTipoDesconto;
	}
	public int getCdTipoDesconto(){
		return this.cdTipoDesconto;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdFaixaDesconto(int cdFaixaDesconto){
		this.cdFaixaDesconto=cdFaixaDesconto;
	}
	public int getCdFaixaDesconto(){
		return this.cdFaixaDesconto;
	}
	public void setPrDesconto(float prDesconto){
		this.prDesconto=prDesconto;
	}
	public float getPrDesconto(){
		return this.prDesconto;
	}
	public void setDtInicialValidade(GregorianCalendar dtInicialValidade){
		this.dtInicialValidade=dtInicialValidade;
	}
	public GregorianCalendar getDtInicialValidade(){
		return this.dtInicialValidade;
	}
	public void setDtFinalValidade(GregorianCalendar dtFinalValidade){
		this.dtFinalValidade=dtFinalValidade;
	}
	public GregorianCalendar getDtFinalValidade(){
		return this.dtFinalValidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDesconto: " +  getCdTipoDesconto();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdFaixaDesconto: " +  getCdFaixaDesconto();
		valueToString += ", prDesconto: " +  getPrDesconto();
		valueToString += ", dtInicialValidade: " +  sol.util.Util.formatDateTime(getDtInicialValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalValidade: " +  sol.util.Util.formatDateTime(getDtFinalValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FaixaDesconto(getCdTipoDesconto(),
			getCdEmpresa(),
			getCdFaixaDesconto(),
			getPrDesconto(),
			getDtInicialValidade()==null ? null : (GregorianCalendar)getDtInicialValidade().clone(),
			getDtFinalValidade()==null ? null : (GregorianCalendar)getDtFinalValidade().clone());
	}

}
