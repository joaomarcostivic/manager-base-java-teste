package com.tivic.manager.blb;

import java.util.GregorianCalendar;

public class ExemplarOcorrencia {

	private int cdOcorrencia;
	private int cdExemplar;
	private int cdPublicacao;
	private int cdOperador;
	private int cdPessoa;
	private int tpOcorrencia;
	private GregorianCalendar dtOcorrencia;
	private GregorianCalendar dtLimite;
	private GregorianCalendar dtFinal;
	private String txtObservacao;
	
	public static final int TP_OCORRENCIA_EMPRESTIMO = 0;

	public ExemplarOcorrencia(){ }

	public ExemplarOcorrencia(int cdOcorrencia,
			int cdExemplar,
			int cdPublicacao,
			int cdOperador,
			int cdPessoa,
			int tpOcorrencia,
			GregorianCalendar dtOcorrencia,
			GregorianCalendar dtLimite,
			GregorianCalendar dtFinal,
			String txtObservacao){
		setCdOcorrencia(cdOcorrencia);
		setCdExemplar(cdExemplar);
		setCdPublicacao(cdPublicacao);
		setCdOperador(cdOperador);
		setCdPessoa(cdPessoa);
		setTpOcorrencia(tpOcorrencia);
		setDtOcorrencia(dtOcorrencia);
		setDtLimite(dtLimite);
		setDtFinal(dtFinal);
		setTxtObservacao(txtObservacao);
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setCdExemplar(int cdExemplar){
		this.cdExemplar=cdExemplar;
	}
	public int getCdExemplar(){
		return this.cdExemplar;
	}
	public void setCdPublicacao(int cdPublicacao){
		this.cdPublicacao=cdPublicacao;
	}
	public int getCdPublicacao(){
		return this.cdPublicacao;
	}
	public void setCdOperador(int cdOperador){
		this.cdOperador=cdOperador;
	}
	public int getCdOperador(){
		return this.cdOperador;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpOcorrencia(int tpOcorrencia){
		this.tpOcorrencia=tpOcorrencia;
	}
	public int getTpOcorrencia(){
		return this.tpOcorrencia;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setDtLimite(GregorianCalendar dtLimite){
		this.dtLimite=dtLimite;
	}
	public GregorianCalendar getDtLimite(){
		return this.dtLimite;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdExemplar: " +  getCdExemplar();
		valueToString += ", cdPublicacao: " +  getCdPublicacao();
		valueToString += ", cdOperador: " +  getCdOperador();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", tpOcorrencia: " +  getTpOcorrencia();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLimite: " +  sol.util.Util.formatDateTime(getDtLimite(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ExemplarOcorrencia(getCdOcorrencia(),
			getCdExemplar(),
			getCdPublicacao(),
			getCdOperador(),
			getCdPessoa(),
			getTpOcorrencia(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getDtLimite()==null ? null : (GregorianCalendar)getDtLimite().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getTxtObservacao());
	}

}