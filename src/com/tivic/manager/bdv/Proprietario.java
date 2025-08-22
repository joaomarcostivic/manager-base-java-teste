package com.tivic.manager.bdv;

import java.util.GregorianCalendar;

public class Proprietario {

	private int cdProprietario;
	private int cdVeiculo;
	private String nmProprietario;
	private String nrCpf;
	private String nrCnh;
	private String sgUfCnh;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private int stProprietario;
	private int tpProprietario;

	public Proprietario() { }

	public Proprietario(int cdProprietario,
			int cdVeiculo,
			String nmProprietario,
			String nrCpf,
			String nrCnh,
			String sgUfCnh,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int stProprietario,
			int tpProprietario) {
		setCdProprietario(cdProprietario);
		setCdVeiculo(cdVeiculo);
		setNmProprietario(nmProprietario);
		setNrCpf(nrCpf);
		setNrCnh(nrCnh);
		setSgUfCnh(sgUfCnh);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setStProprietario(stProprietario);
		setTpProprietario(tpProprietario);
	}
	public void setCdProprietario(int cdProprietario){
		this.cdProprietario=cdProprietario;
	}
	public int getCdProprietario(){
		return this.cdProprietario;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setNmProprietario(String nmProprietario){
		this.nmProprietario=nmProprietario;
	}
	public String getNmProprietario(){
		return this.nmProprietario;
	}
	public void setNrCpf(String nrCpf){
		this.nrCpf=nrCpf;
	}
	public String getNrCpf(){
		return this.nrCpf;
	}
	public void setNrCnh(String nrCnh){
		this.nrCnh=nrCnh;
	}
	public String getNrCnh(){
		return this.nrCnh;
	}
	public void setSgUfCnh(String sgUfCnh){
		this.sgUfCnh=sgUfCnh;
	}
	public String getSgUfCnh(){
		return this.sgUfCnh;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setStProprietario(int stProprietario){
		this.stProprietario=stProprietario;
	}
	public int getStProprietario(){
		return this.stProprietario;
	}
	public void setTpProprietario(int tpProprietario){
		this.tpProprietario=tpProprietario;
	}
	public int getTpProprietario(){
		return this.tpProprietario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProprietario: " +  getCdProprietario();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", nmProprietario: " +  getNmProprietario();
		valueToString += ", nrCpf: " +  getNrCpf();
		valueToString += ", nrCnh: " +  getNrCnh();
		valueToString += ", sgUfCnh: " +  getSgUfCnh();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stProprietario: " +  getStProprietario();
		valueToString += ", tpProprietario: " +  getTpProprietario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Proprietario(getCdProprietario(),
			getCdVeiculo(),
			getNmProprietario(),
			getNrCpf(),
			getNrCnh(),
			getSgUfCnh(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getStProprietario(),
			getTpProprietario());
	}

}