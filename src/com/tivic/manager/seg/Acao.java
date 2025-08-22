package com.tivic.manager.seg;

import java.util.List;

public class Acao {

	private int cdAcao;
	private int cdModulo;
	private int cdSistema;
	private String nmAcao;
	private String dsAcao;
	private int cdAgrupamento;
	private String idAcao;
	private int tpOrganizacao;
	private int nrOrdem;
	private int cdAcaoSuperior;

	private List<Acao> acoes;
	
	public Acao(){ }

	public Acao(int cdAcao,
			int cdModulo,
			int cdSistema,
			String nmAcao,
			String dsAcao,
			int cdAgrupamento,
			String idAcao,
			int tpOrganizacao,
			int nrOrdem,
			int cdAcaoSuperior){
		setCdAcao(cdAcao);
		setCdModulo(cdModulo);
		setCdSistema(cdSistema);
		setNmAcao(nmAcao);
		setDsAcao(dsAcao);
		setCdAgrupamento(cdAgrupamento);
		setIdAcao(idAcao);
		setTpOrganizacao(tpOrganizacao);
		setNrOrdem(nrOrdem);
		setCdAcaoSuperior(cdAcaoSuperior);
	}
	public void setCdAcao(int cdAcao){
		this.cdAcao=cdAcao;
	}
	public int getCdAcao(){
		return this.cdAcao;
	}
	public void setCdModulo(int cdModulo){
		this.cdModulo=cdModulo;
	}
	public int getCdModulo(){
		return this.cdModulo;
	}
	public void setCdSistema(int cdSistema){
		this.cdSistema=cdSistema;
	}
	public int getCdSistema(){
		return this.cdSistema;
	}
	public void setNmAcao(String nmAcao){
		this.nmAcao=nmAcao;
	}
	public String getNmAcao(){
		return this.nmAcao;
	}
	public void setDsAcao(String dsAcao){
		this.dsAcao=dsAcao;
	}
	public String getDsAcao(){
		return this.dsAcao;
	}
	public void setCdAgrupamento(int cdAgrupamento){
		this.cdAgrupamento=cdAgrupamento;
	}
	public int getCdAgrupamento(){
		return this.cdAgrupamento;
	}
	public void setIdAcao(String idAcao){
		this.idAcao=idAcao;
	}
	public String getIdAcao(){
		return this.idAcao;
	}
	public void setTpOrganizacao(int tpOrganizacao){
		this.tpOrganizacao=tpOrganizacao;
	}
	public int getTpOrganizacao(){
		return this.tpOrganizacao;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setCdAcaoSuperior(int cdAcaoSuperior){
		this.cdAcaoSuperior=cdAcaoSuperior;
	}
	public int getCdAcaoSuperior(){
		return this.cdAcaoSuperior;
	}
	public void setAcoes(List<Acao> acoes) {
		this.acoes = acoes;
	}
	public List<Acao> getAcoes() {
		return acoes;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAcao: " +  getCdAcao();
		valueToString += ", cdModulo: " +  getCdModulo();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", nmAcao: " +  getNmAcao();
		valueToString += ", dsAcao: " +  getDsAcao();
		valueToString += ", cdAgrupamento: " +  getCdAgrupamento();
		valueToString += ", idAcao: " +  getIdAcao();
		valueToString += ", tpOrganizacao: " +  getTpOrganizacao();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", cdAcaoSuperior: " +  getCdAcaoSuperior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Acao(getCdAcao(),
			getCdModulo(),
			getCdSistema(),
			getNmAcao(),
			getDsAcao(),
			getCdAgrupamento(),
			getIdAcao(),
			getTpOrganizacao(),
			getNrOrdem(),
			getCdAcaoSuperior());
	}

}