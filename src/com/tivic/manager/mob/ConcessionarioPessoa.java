package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConcessionarioPessoa {

	private int cdConcessionarioPessoa;
	private int cdConcessionario;
	private int cdPessoa;
	private int tpVinculo;
	private int stConcessionarioPessoa;
	private String txtObservacao;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtAtivacao;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtInativacao;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtPendencia;

	public ConcessionarioPessoa(){ }

	public ConcessionarioPessoa(int cdConcessionarioPessoa,
			int cdConcessionario,
			int cdPessoa,
			int tpVinculo,
			int stConcessionarioPessoa,
			String txtObservacao,
			GregorianCalendar dtAtivacao,
			GregorianCalendar dtInativacao,
			GregorianCalendar dtPendencia){
		setCdConcessionarioPessoa(cdConcessionarioPessoa);
		setCdConcessionario(cdConcessionario);
		setCdPessoa(cdPessoa);
		setStConcessionarioPessoa(stConcessionarioPessoa);
		setTpVinculo(tpVinculo);
		setTxtObservacao(txtObservacao);
		setDtAtivacao(dtAtivacao);
		setDtInativacao(dtInativacao);
		setDtPendencia(dtPendencia);
	}
	
	public void setCdConcessionarioPessoa(int cdConcessionarioPessoa){
		this.cdConcessionarioPessoa=cdConcessionarioPessoa;
	}
	public int getCdConcessionarioPessoa(){
		return this.cdConcessionarioPessoa;
	}
	public void setCdConcessionario(int cdConcessionario){
		this.cdConcessionario=cdConcessionario;
	}
	public int getCdConcessionario(){
		return this.cdConcessionario;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpVinculo(int tpVinculo){
		this.tpVinculo=tpVinculo;
	}
	public int getTpVinculo(){
		return this.tpVinculo;
	}
	public void setStConcessionarioPessoa(int stConcessionarioPessoa){
		this.stConcessionarioPessoa=stConcessionarioPessoa;
	}
	public int getStConcessionarioPessoa(){
		return this.stConcessionarioPessoa;
	}
	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao=txtObservacao;		
	}
	public String getTxtObservacao() {		
		return this.txtObservacao;
	}
	public void setDtAtivacao(GregorianCalendar dtAtivacao) {
		this.dtAtivacao=dtAtivacao;		
	}
	public GregorianCalendar getDtAtivacao() {		
		return this.dtAtivacao;
	}
	public void setDtInativacao(GregorianCalendar dtInativacao) {
		this.dtInativacao=dtInativacao;	
	}
	public GregorianCalendar getDtInativacao() {
		return this.dtInativacao;
	}
	public void setDtPendencia(GregorianCalendar dtPendencia) {
		this.dtPendencia=dtPendencia;		
	}
	public GregorianCalendar getDtPendencia() {		
		return this.dtPendencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessionarioPessoa: " +  getCdConcessionarioPessoa();
		valueToString += ", cdConcessionario: " +  getCdConcessionario();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", tpVinculo: " +  getTpVinculo();
		valueToString += ", stConcessionarioPessoa: " +  getStConcessionarioPessoa();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", dtAtivacao: " +  sol.util.Util.formatDateTime(getDtAtivacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtInativacao: " +  sol.util.Util.formatDateTime(getDtInativacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtPendencia: " +  sol.util.Util.formatDateTime(getDtPendencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}


	public Object clone() {
		return new ConcessionarioPessoa(getCdConcessionarioPessoa(),
			getCdConcessionario(),
			getCdPessoa(),
			getTpVinculo(),
			getStConcessionarioPessoa(),
			getTxtObservacao(),
			getDtAtivacao()==null ? null : (GregorianCalendar)getDtAtivacao().clone(),
			getDtInativacao()==null ? null : (GregorianCalendar)getDtInativacao().clone(),
			getDtPendencia()==null ? null : (GregorianCalendar)getDtPendencia().clone());
	}

}