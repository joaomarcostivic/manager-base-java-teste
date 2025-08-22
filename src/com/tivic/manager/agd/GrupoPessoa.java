package com.tivic.manager.agd;

import java.util.GregorianCalendar;

public class GrupoPessoa {

	private int cdPessoa;
	private int cdGrupo;
	private GregorianCalendar dtInclusao;
	private int stGrupoPessoa;

	public GrupoPessoa(){ }
	
	public GrupoPessoa(int cdPessoa,
			int cdGrupo,
			GregorianCalendar dtInclusao,
			int stGrupoPessoa){
		setCdPessoa(cdPessoa);
		setCdGrupo(cdGrupo);
		setDtInclusao(dtInclusao);
		setStGrupoPessoa(stGrupoPessoa);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setDtInclusao(GregorianCalendar dtInclusao){
		this.dtInclusao=dtInclusao;
	}
	public GregorianCalendar getDtInclusao(){
		return this.dtInclusao;
	}
	public void setStGrupoPessoa(int stGrupoPessoa){
		this.stGrupoPessoa=stGrupoPessoa;
	}
	public int getStGrupoPessoa(){
		return this.stGrupoPessoa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", dtInclusao: " +  sol.util.Util.formatDateTime(getDtInclusao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stGrupoPessoa: " +  getStGrupoPessoa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoPessoa(getCdPessoa(),
			getCdGrupo(),
			getDtInclusao()==null ? null : (GregorianCalendar)getDtInclusao().clone(),
			getStGrupoPessoa());
	}

}
