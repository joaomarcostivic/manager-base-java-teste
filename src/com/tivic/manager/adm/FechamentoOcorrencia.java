package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class FechamentoOcorrencia {

	private int cdFechamentoOcorrencia;
	private int cdUsuario;
	private int cdFechamento;
	private int cdConta;
	private String dsOcorrencia;
	private GregorianCalendar dtOcorrencia;
	private int cdMovimentoConta;

	public FechamentoOcorrencia() { }

	public FechamentoOcorrencia(int cdFechamentoOcorrencia,
			int cdUsuario,
			int cdFechamento,
			int cdConta,
			String dsOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdMovimentoConta) {
		setCdFechamentoOcorrencia(cdFechamentoOcorrencia);
		setCdUsuario(cdUsuario);
		setCdFechamento(cdFechamento);
		setCdConta(cdConta);
		setDsOcorrencia(dsOcorrencia);
		setDtOcorrencia(dtOcorrencia);
		setCdMovimentoConta(cdMovimentoConta);
	}
	public void setCdFechamentoOcorrencia(int cdFechamentoOcorrencia){
		this.cdFechamentoOcorrencia=cdFechamentoOcorrencia;
	}
	public int getCdFechamentoOcorrencia(){
		return this.cdFechamentoOcorrencia;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdFechamento(int cdFechamento){
		this.cdFechamento=cdFechamento;
	}
	public int getCdFechamento(){
		return this.cdFechamento;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setDsOcorrencia(String dsOcorrencia){
		this.dsOcorrencia=dsOcorrencia;
	}
	public String getDsOcorrencia(){
		return this.dsOcorrencia;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setCdMovimentoConta(int cdMovimentoConta){
		this.cdMovimentoConta=cdMovimentoConta;
	}
	public int getCdMovimentoConta(){
		return this.cdMovimentoConta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFechamentoOcorrencia: " +  getCdFechamentoOcorrencia();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdFechamento: " +  getCdFechamento();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", dsOcorrencia: " +  getDsOcorrencia();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdMovimentoConta: " +  getCdMovimentoConta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FechamentoOcorrencia(getCdFechamentoOcorrencia(),
			getCdUsuario(),
			getCdFechamento(),
			getCdConta(),
			getDsOcorrencia(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getCdMovimentoConta());
	}

}