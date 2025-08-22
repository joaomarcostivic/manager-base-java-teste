package com.tivic.manager.evt;

import java.util.GregorianCalendar;

public class Ocorrencia {

	private int cdOcorrencia;
	private int cdPessoa;
	private int cdEvento;
	private int tpOcorrencia;
	private String txtOcorrencia;
	private GregorianCalendar dtOcorrencia;

	public Ocorrencia(){ }

	public Ocorrencia(int cdOcorrencia,
			int cdPessoa,
			int cdEvento,
			int tpOcorrencia,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia){
		setCdOcorrencia(cdOcorrencia);
		setCdPessoa(cdPessoa);
		setCdEvento(cdEvento);
		setTpOcorrencia(tpOcorrencia);
		setTxtOcorrencia(txtOcorrencia);
		setDtOcorrencia(dtOcorrencia);
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdEvento(int cdEvento){
		this.cdEvento=cdEvento;
	}
	public int getCdEvento(){
		return this.cdEvento;
	}
	public void setTpOcorrencia(int tpOcorrencia){
		this.tpOcorrencia=tpOcorrencia;
	}
	public int getTpOcorrencia(){
		return this.tpOcorrencia;
	}
	public void setTxtOcorrencia(String txtOcorrencia){
		this.txtOcorrencia=txtOcorrencia;
	}
	public String getTxtOcorrencia(){
		return this.txtOcorrencia;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdEvento: " +  getCdEvento();
		valueToString += ", tpOcorrencia: " +  getTpOcorrencia();
		valueToString += ", txtOcorrencia: " +  getTxtOcorrencia();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ocorrencia(getCdOcorrencia(),
			getCdPessoa(),
			getCdEvento(),
			getTpOcorrencia(),
			getTxtOcorrencia(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone());
	}

}