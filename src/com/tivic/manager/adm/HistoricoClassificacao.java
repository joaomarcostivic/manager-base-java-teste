package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class HistoricoClassificacao {

	private int cdPessoa;
	private int cdClassificacao;
	private GregorianCalendar dtInicio;

	public HistoricoClassificacao(int cdPessoa,
			int cdClassificacao,
			GregorianCalendar dtInicio){
		setCdPessoa(cdPessoa);
		setCdClassificacao(cdClassificacao);
		setDtInicio(dtInicio);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdClassificacao(int cdClassificacao){
		this.cdClassificacao=cdClassificacao;
	}
	public int getCdClassificacao(){
		return this.cdClassificacao;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdClassificacao: " +  getCdClassificacao();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new HistoricoClassificacao(getCdPessoa(),
			getCdClassificacao(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone());
	}

}
