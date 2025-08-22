package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class CondicaoPagamento {
	private int cdCondicaoPagamento;
	private float vlLimite;
	private float vlLimiteMensal;
	private float vlTroco;
	private String idCondicaoPagamento;
	private int stCondicaoPagamento;
	private int lgPadrao;
	private String nmCondicaoPagamento;
	private String txtDescricao;
	private GregorianCalendar dtValidadeCondicao;
	private GregorianCalendar dtValidadeLimite;
	private int lgPermiteTroco;
	
	public CondicaoPagamento(int cdCondicaoPagamento,
			float vlLimite,
			float vlLimiteMensal,
			float vlTroco,
			String idCondicaoPagamento,
			int stCondicaoPagamento,
			int lgPadrao,
			String nmCondicaoPagamento,
			String txtDescricao,
			GregorianCalendar dtValidadeCondicao,
			GregorianCalendar dtValidadeLimite,
			int lgPermiteTroco){
		setCdCondicaoPagamento(cdCondicaoPagamento);
		setVlLimite(vlLimite);
		setVlLimiteMensal(vlLimiteMensal);
		setVlTroco(vlTroco);
		setIdCondicaoPagamento(idCondicaoPagamento);
		setStCondicaoPagamento(stCondicaoPagamento);
		setLgPadrao(lgPadrao);
		setNmCondicaoPagamento(nmCondicaoPagamento);
		setTxtDescricao(txtDescricao);
		setDtValidadeCondicao(dtValidadeCondicao);
		setDtValidadeLimite(dtValidadeLimite);
		setLgPermiteTroco(lgPermiteTroco);
	}
	
	public void setCdCondicaoPagamento(int cdCondicaoPagamento){
		this.cdCondicaoPagamento=cdCondicaoPagamento;
	}
	public int getCdCondicaoPagamento(){
		return this.cdCondicaoPagamento;
	}
	public void setVlLimite(float vlLimite){
		this.vlLimite=vlLimite;
	}
	public float getVlLimite(){
		return this.vlLimite;
	}
	public void setVlLimiteMensal(float vlLimiteMensal){
		this.vlLimiteMensal=vlLimiteMensal;
	}
	public float getVlLimiteMensal(){
		return this.vlLimiteMensal;
	}
	public void setVlTroco(float vlTroco){
		this.vlTroco=vlTroco;
	}
	public float getVlTroco(){
		return this.vlTroco;
	}
	public void setIdCondicaoPagamento(String idCondicaoPagamento){
		this.idCondicaoPagamento=idCondicaoPagamento;
	}
	public String getIdCondicaoPagamento(){
		return this.idCondicaoPagamento;
	}
	public void setStCondicaoPagamento(int stCondicaoPagamento){
		this.stCondicaoPagamento=stCondicaoPagamento;
	}
	public int getStCondicaoPagamento(){
		return this.stCondicaoPagamento;
	}
	public void setLgPadrao(int lgPadrao){
		this.lgPadrao=lgPadrao;
	}
	public int getLgPadrao(){
		return this.lgPadrao;
	}
	public void setNmCondicaoPagamento(String nmCondicaoPagamento){
		this.nmCondicaoPagamento=nmCondicaoPagamento;
	}
	public String getNmCondicaoPagamento(){
		return this.nmCondicaoPagamento;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public void setDtValidadeCondicao(GregorianCalendar dtValidadeCondicao){
		this.dtValidadeCondicao=dtValidadeCondicao;
	}
	public GregorianCalendar getDtValidadeCondicao(){
		return this.dtValidadeCondicao;
	}
	public void setDtValidadeLimite(GregorianCalendar dtValidadeLimite){
		this.dtValidadeLimite=dtValidadeLimite;
	}
	public GregorianCalendar getDtValidadeLimite(){
		return this.dtValidadeLimite;
	}
	public void setLgPermiteTroco(int lgPermiteTroco){
		this.lgPermiteTroco=lgPermiteTroco;
	}
	public int getLgPermiteTroco(){
		return this.lgPermiteTroco;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCondicaoPagamento: " +  getCdCondicaoPagamento();
		valueToString += ", vlLimite: " +  getVlLimite();
		valueToString += ", vlLimiteMensal: " +  getVlLimiteMensal();
		valueToString += ", vlTroco: " +  getVlTroco();
		valueToString += ", idCondicaoPagamento: " +  getIdCondicaoPagamento();
		valueToString += ", stCondicaoPagamento: " +  getStCondicaoPagamento();
		valueToString += ", lgPadrao: " +  getLgPadrao();
		valueToString += ", nmCondicaoPagamento	: " +  getNmCondicaoPagamento();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", dtValidadeCondicao: " +  sol.util.Util.formatDateTime(getDtValidadeCondicao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtValidadeLimite: " +  sol.util.Util.formatDateTime(getDtValidadeLimite(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgPermiteTroco: " +  getLgPermiteTroco();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CondicaoPagamento(getCdCondicaoPagamento(),
			getVlLimite(),
			getVlLimiteMensal(),
			getVlTroco(),
			getIdCondicaoPagamento(),
			getStCondicaoPagamento(),
			getLgPadrao(),
			getNmCondicaoPagamento(),
			getTxtDescricao(),
			getDtValidadeCondicao()==null ? null : (GregorianCalendar)getDtValidadeCondicao().clone(),
			getDtValidadeLimite()==null ? null : (GregorianCalendar)getDtValidadeLimite().clone(),
			getLgPermiteTroco());
	}
}
