package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class PessoaEmpresa {

	private int cdEmpresa;
	private int cdPessoa;
	private int cdVinculo;
	private GregorianCalendar dtVinculo;
	private int stVinculo;

	public PessoaEmpresa(int cdEmpresa,
			int cdPessoa,
			int cdVinculo,
			GregorianCalendar dtVinculo,
			int stVinculo){
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setCdVinculo(cdVinculo);
		setDtVinculo(dtVinculo);
		setStVinculo(stVinculo);
	}
	public PessoaEmpresa() { }
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public void setDtVinculo(GregorianCalendar dtVinculo){
		this.dtVinculo=dtVinculo;
	}
	public GregorianCalendar getDtVinculo(){
		return this.dtVinculo;
	}
	public void setStVinculo(int stVinculo){
		this.stVinculo=stVinculo;
	}
	public int getStVinculo(){
		return this.stVinculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdVinculo: " +  getCdVinculo();
		valueToString += ", dtVinculo: " +  sol.util.Util.formatDateTime(getDtVinculo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stVinculo: " +  getStVinculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaEmpresa(getCdEmpresa(),
			getCdPessoa(),
			getCdVinculo(),
			getDtVinculo()==null ? null : (GregorianCalendar)getDtVinculo().clone(),
			getStVinculo());
	}

}
