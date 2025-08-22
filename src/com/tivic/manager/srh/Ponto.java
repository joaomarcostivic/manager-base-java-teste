package com.tivic.manager.srh;

import java.util.GregorianCalendar;

public class Ponto {

	private int cdPonto;
	private int cdMatricula;
	private GregorianCalendar dtAbertura;
	private GregorianCalendar dtFechamento;
	private int stPonto;
	private String txtObservacao;

	public Ponto(int cdPonto,
			int cdMatricula,
			GregorianCalendar dtAbertura,
			GregorianCalendar dtFechamento,
			int stPonto,
			String txtObservacao){
		setCdPonto(cdPonto);
		setCdMatricula(cdMatricula);
		setDtAbertura(dtAbertura);
		setDtFechamento(dtFechamento);
		setStPonto(stPonto);
		setTxtObservacao(txtObservacao);
	}
	public void setCdPonto(int cdPonto){
		this.cdPonto=cdPonto;
	}
	public int getCdPonto(){
		return this.cdPonto;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setDtAbertura(GregorianCalendar dtAbertura){
		this.dtAbertura=dtAbertura;
	}
	public GregorianCalendar getDtAbertura(){
		return this.dtAbertura;
	}
	public void setDtFechamento(GregorianCalendar dtFechamento){
		this.dtFechamento=dtFechamento;
	}
	public GregorianCalendar getDtFechamento(){
		return this.dtFechamento;
	}
	public void setStPonto(int stPonto){
		this.stPonto=stPonto;
	}
	public int getStPonto(){
		return this.stPonto;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPonto: " +  getCdPonto();
		valueToString += ", cdMatricula: " +  getCdMatricula();
		valueToString += ", dtAbertura: " +  sol.util.Util.formatDateTime(getDtAbertura(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFechamento: " +  sol.util.Util.formatDateTime(getDtFechamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stPonto: " +  getStPonto();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ponto(getCdPonto(),
			getCdMatricula(),
			getDtAbertura()==null ? null : (GregorianCalendar)getDtAbertura().clone(),
			getDtFechamento()==null ? null : (GregorianCalendar)getDtFechamento().clone(),
			getStPonto(),
			getTxtObservacao());
	}

}