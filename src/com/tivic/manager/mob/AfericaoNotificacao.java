package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class AfericaoNotificacao {

	private int cdAfericaoNotificacao;
	private int stAfericaoNotificacao;
	private GregorianCalendar dtAfericaoNotificacao;
	private int cdInfracao;
	private int cdMotivo;
	private int cdControle;
	private int cdAit;
	private String dsCancelamento;

	public AfericaoNotificacao() { }

	public AfericaoNotificacao(int cdAfericaoNotificacao,
			int stAfericaoNotificacao,
			GregorianCalendar dtAfericaoNotificacao,
			int cdInfracao,
			int cdMotivo,
			int cdControle,
			int cdAit,
			String dsCancelamento) {
		setCdAfericaoNotificacao(cdAfericaoNotificacao);
		setStAfericaoNotificacao(stAfericaoNotificacao);
		setDtAfericaoNotificacao(dtAfericaoNotificacao);
		setCdInfracao(cdInfracao);
		setCdMotivo(cdMotivo);
		setCdControle(cdControle);
		setCdAit(cdAit);
		setDsCancelamento(dsCancelamento);
	}
	public void setCdAfericaoNotificacao(int cdAfericaoNotificacao){
		this.cdAfericaoNotificacao=cdAfericaoNotificacao;
	}
	public int getCdAfericaoNotificacao(){
		return this.cdAfericaoNotificacao;
	}
	public void setStAfericaoNotificacao(int stAfericaoNotificacao){
		this.stAfericaoNotificacao=stAfericaoNotificacao;
	}
	public int getStAfericaoNotificacao(){
		return this.stAfericaoNotificacao;
	}
	public void setDtAfericaoNotificacao(GregorianCalendar dtAfericaoNotificacao){
		this.dtAfericaoNotificacao=dtAfericaoNotificacao;
	}
	public GregorianCalendar getDtAfericaoNotificacao(){
		return this.dtAfericaoNotificacao;
	}
	public void setCdInfracao(int cdInfracao){
		this.cdInfracao=cdInfracao;
	}
	public int getCdInfracao(){
		return this.cdInfracao;
	}
	public void setCdMotivo(int cdMotivo){
		this.cdMotivo=cdMotivo;
	}
	public int getCdMotivo(){
		return this.cdMotivo;
	}
	public void setCdControle(int cdControle){
		this.cdControle=cdControle;
	}
	public int getCdControle(){
		return this.cdControle;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setDsCancelamento(String dsCancelamento){
		this.dsCancelamento=dsCancelamento;
	}
	public String getDsCancelamento(){
		return this.dsCancelamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAfericaoNotificacao: " +  getCdAfericaoNotificacao();
		valueToString += ", stAfericaoNotificacao: " +  getStAfericaoNotificacao();
		valueToString += ", dtAfericaoNotificacao: " +  sol.util.Util.formatDateTime(getDtAfericaoNotificacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdInfracao: " +  getCdInfracao();
		valueToString += ", cdMotivo: " +  getCdMotivo();
		valueToString += ", cdControle: " +  getCdControle();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", dsCancelamento: " +  getDsCancelamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AfericaoNotificacao(getCdAfericaoNotificacao(),
			getStAfericaoNotificacao(),
			getDtAfericaoNotificacao()==null ? null : (GregorianCalendar)getDtAfericaoNotificacao().clone(),
			getCdInfracao(),
			getCdMotivo(),
			getCdControle(),
			getCdAit(),
			getDsCancelamento());
	}

}