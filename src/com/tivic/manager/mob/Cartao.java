package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class Cartao {

	private int cdCartao;
	private int cdPessoa;
	private int stCartao;
	private int tpCartao;
	private int nrVia;
	private String idCartao;
	private GregorianCalendar dtEmissao;
	private GregorianCalendar dtValidade;
	private int lgAcompanhante;
	private int tpVigencia;

	public Cartao(){ }

	public Cartao(int cdCartao,
			int cdPessoa,
			int stCartao,
			int tpCartao,
			int nrVia,
			String idCartao,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtValidade,
			int lgAcompanhante,
			int tpVigencia){
		setCdCartao(cdCartao);
		setCdPessoa(cdPessoa);
		setStCartao(stCartao);
		setTpCartao(tpCartao);
		setNrVia(nrVia);
		setIdCartao(idCartao);
		setDtEmissao(dtEmissao);
		setDtValidade(dtValidade);
	}
	public void setCdCartao(int cdCartao){
		this.cdCartao=cdCartao;
	}
	public int getCdCartao(){
		return this.cdCartao;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setStCartao(int stCartao){
		this.stCartao=stCartao;
	}
	public int getStCartao(){
		return this.stCartao;
	}
	public void setTpCartao(int tpCartao){
		this.tpCartao=tpCartao;
	}
	public int getTpCartao(){
		return this.tpCartao;
	}
	public void setNrVia(int nrVia){
		this.nrVia=nrVia;
	}
	public int getNrVia(){
		return this.nrVia;
	}
	public void setIdCartao(String idCartao){
		this.idCartao=idCartao;
	}
	public String getIdCartao(){
		return this.idCartao;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao){
		this.dtEmissao=dtEmissao;
	}
	public GregorianCalendar getDtEmissao(){
		return this.dtEmissao;
	}
	public void setDtValidade(GregorianCalendar dtValidade){
		this.dtValidade=dtValidade;
	}
	public GregorianCalendar getDtValidade(){
		return this.dtValidade;
	}
	public void setLgAcompanhante(int lgAcompanhante){
		this.lgAcompanhante=lgAcompanhante;
	}
	public int getLgAcompanhante(){
		return this.lgAcompanhante;
	}
	public void setTpVigencia(int tpVigencia){
		this.tpVigencia=tpVigencia;
	}
	public int getTpVigencia(){
		return this.tpVigencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCartao: " +  getCdCartao();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", stCartao: " +  getStCartao();
		valueToString += ", tpCartao: " +  getTpCartao();
		valueToString += ", nrVia: " +  getNrVia();
		valueToString += ", idCartao: " +  getIdCartao();
		valueToString += ", dtEmissao: " +  sol.util.Util.formatDateTime(getDtEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtValidade: " +  sol.util.Util.formatDateTime(getDtValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgAcompanhante: " +  getLgAcompanhante();
		valueToString += ", tpVigencia: " +  getTpVigencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cartao(getCdCartao(),
			getCdPessoa(),
			getStCartao(),
			getTpCartao(),
			getNrVia(),
			getIdCartao(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone(),
			getDtValidade()==null ? null : (GregorianCalendar)getDtValidade().clone(),
			getLgAcompanhante(),
			getTpVigencia());
	}

}