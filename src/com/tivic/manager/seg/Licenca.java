package com.tivic.manager.seg;

import java.util.GregorianCalendar;

public class Licenca {

	private int cdLicenca;
	private int cdPessoa;
	private String idUnico;
	private GregorianCalendar dtCadastro;

	public Licenca(){ }

	public Licenca(int cdLicenca,
			int cdPessoa,
			String idUnico,
			GregorianCalendar dtCadastro){
		setCdLicenca(cdLicenca);
		setCdPessoa(cdPessoa);
		setIdUnico(idUnico);
		setDtCadastro(dtCadastro);
	}
	public void setCdLicenca(int cdLicenca){
		this.cdLicenca=cdLicenca;
	}
	public int getCdLicenca(){
		return this.cdLicenca;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setIdUnico(String idUnico){
		this.idUnico=idUnico;
	}
	public String getIdUnico(){
		return this.idUnico;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLicenca: " +  getCdLicenca();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", idUnico: " +  getIdUnico();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Licenca(getCdLicenca(),
			getCdPessoa(),
			getIdUnico(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone());
	}

}
