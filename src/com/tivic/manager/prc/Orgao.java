package com.tivic.manager.prc;

public class Orgao {

	private int cdOrgao;
	private int cdTipoOrgao;
	private String nmOrgao;
	private String idOrgao;
	private int cdResponsavel;
	private int cdPessoa;
	private int tpContratacao;

	public Orgao(){ }

	public Orgao(int cdOrgao,
			int cdTipoOrgao,
			String nmOrgao,
			String idOrgao,
			int cdResponsavel,
			int cdPessoa,
			int tpContratacao){
		setCdOrgao(cdOrgao);
		setCdTipoOrgao(cdTipoOrgao);
		setNmOrgao(nmOrgao);
		setIdOrgao(idOrgao);
		setCdResponsavel(cdResponsavel);
		setCdPessoa(cdPessoa);
		setTpContratacao(tpContratacao);
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setCdTipoOrgao(int cdTipoOrgao){
		this.cdTipoOrgao=cdTipoOrgao;
	}
	public int getCdTipoOrgao(){
		return this.cdTipoOrgao;
	}
	public void setNmOrgao(String nmOrgao){
		this.nmOrgao=nmOrgao;
	}
	public String getNmOrgao(){
		return this.nmOrgao;
	}
	public void setIdOrgao(String idOrgao){
		this.idOrgao=idOrgao;
	}
	public String getIdOrgao(){
		return this.idOrgao;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpContratacao(int tpContratacao){
		this.tpContratacao=tpContratacao;
	}
	public int getTpContratacao(){
		return this.tpContratacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrgao: " +  getCdOrgao();
		valueToString += ", cdTipoOrgao: " +  getCdTipoOrgao();
		valueToString += ", nmOrgao: " +  getNmOrgao();
		valueToString += ", idOrgao: " +  getIdOrgao();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", tpContratacao: " + getTpContratacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Orgao(getCdOrgao(),
			getCdTipoOrgao(),
			getNmOrgao(),
			getIdOrgao(),
			getCdResponsavel(),
			getCdPessoa(),
			getTpContratacao());
	}

}