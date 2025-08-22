package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class LinhaPassageiro {

	private int cdLinhaPassageiro;
	private int cdLinha;
	private int cdPessoa;
	private int cdMatricula;
	private GregorianCalendar dtInicio;
	private GregorianCalendar dtFim;

	public LinhaPassageiro() { }

	public LinhaPassageiro(int cdLinhaPassageiro,
			int cdLinha,
			int cdPessoa,
			int cdMatricula,
			GregorianCalendar dtInicio,
			GregorianCalendar dtFim) {
		setCdLinhaPassageiro(cdLinhaPassageiro);
		setCdLinha(cdLinha);
		setCdPessoa(cdPessoa);
		setCdMatricula(cdMatricula);
		setDtInicio(dtInicio);
		setDtFim(dtFim);
	}
	public void setCdLinhaPassageiro(int cdLinhaPassageiro){
		this.cdLinhaPassageiro=cdLinhaPassageiro;
	}
	public int getCdLinhaPassageiro(){
		return this.cdLinhaPassageiro;
	}
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	public int getCdLinha(){
		return this.cdLinha;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setDtFim(GregorianCalendar dtFim){
		this.dtFim=dtFim;
	}
	public GregorianCalendar getDtFim(){
		return this.dtFim;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLinhaPassageiro: " +  getCdLinhaPassageiro();
		valueToString += ", cdLinha: " +  getCdLinha();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdMatricula: " +  getCdMatricula();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFim: " +  sol.util.Util.formatDateTime(getDtFim(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LinhaPassageiro(getCdLinhaPassageiro(),
			getCdLinha(),
			getCdPessoa(),
			getCdMatricula(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getDtFim()==null ? null : (GregorianCalendar)getDtFim().clone());
	}

}