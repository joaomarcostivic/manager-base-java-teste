package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class InstituicaoGrupo {

	private int cdGrupo;
	private int cdInstituicao;
	private int cdTipoGrupo;
	private String nmGrupo;
	private String sgGrupo;
	private String txtObservacao;
	private int stGrupo;
	private GregorianCalendar dtCriacao;
	private GregorianCalendar dtExtincao;

	public InstituicaoGrupo(){ }

	public InstituicaoGrupo(int cdGrupo,
			int cdInstituicao,
			int cdTipoGrupo,
			String nmGrupo,
			String sgGrupo,
			String txtObservacao,
			int stGrupo,
			GregorianCalendar dtCriacao,
			GregorianCalendar dtExtincao){
		setCdGrupo(cdGrupo);
		setCdInstituicao(cdInstituicao);
		setCdTipoGrupo(cdTipoGrupo);
		setNmGrupo(nmGrupo);
		setSgGrupo(sgGrupo);
		setTxtObservacao(txtObservacao);
		setStGrupo(stGrupo);
		setDtCriacao(dtCriacao);
		setDtExtincao(dtExtincao);
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdTipoGrupo(int cdTipoGrupo){
		this.cdTipoGrupo=cdTipoGrupo;
	}
	public int getCdTipoGrupo(){
		return this.cdTipoGrupo;
	}
	public void setNmGrupo(String nmGrupo){
		this.nmGrupo=nmGrupo;
	}
	public String getNmGrupo(){
		return this.nmGrupo;
	}
	public void setSgGrupo(String sgGrupo){
		this.sgGrupo=sgGrupo;
	}
	public String getSgGrupo(){
		return this.sgGrupo;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setStGrupo(int stGrupo){
		this.stGrupo=stGrupo;
	}
	public int getStGrupo(){
		return this.stGrupo;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	public void setDtExtincao(GregorianCalendar dtExtincao){
		this.dtExtincao=dtExtincao;
	}
	public GregorianCalendar getDtExtincao(){
		return this.dtExtincao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdTipoGrupo: " +  getCdTipoGrupo();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		valueToString += ", sgGrupo: " +  getSgGrupo();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", stGrupo: " +  getStGrupo();
		valueToString += ", dtCriacao: " +  sol.util.Util.formatDateTime(getDtCriacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtExtincao: " +  sol.util.Util.formatDateTime(getDtExtincao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoGrupo(getCdGrupo(),
			getCdInstituicao(),
			getCdTipoGrupo(),
			getNmGrupo(),
			getSgGrupo(),
			getTxtObservacao(),
			getStGrupo(),
			getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
			getDtExtincao()==null ? null : (GregorianCalendar)getDtExtincao().clone());
	}

}
