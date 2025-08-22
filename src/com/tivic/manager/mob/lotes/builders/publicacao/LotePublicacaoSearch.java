package com.tivic.manager.mob.lotes.builders.publicacao;

public class LotePublicacaoSearch {
	private int tpPublicacao;
	private String idAit;
	private String dtCriacaoInicial;
	private String dtCriacaoFinal;
	private String dtPublicacaoInicial;
	private String dtPublicacaoFinal;
	private int page;
	private int limit;
	
	public int getTpPublicacao() {
		return tpPublicacao;
	}
	public void setTpPublicacao(int tpPublicacao) {
		this.tpPublicacao = tpPublicacao;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public String getDtCriacaoInicial() {
		return dtCriacaoInicial;
	}
	public void setDtCriacaoInicial(String dtCriacaoInicial) {
		this.dtCriacaoInicial = dtCriacaoInicial;
	}
	public String getDtCriacaoFinal() {
		return dtCriacaoFinal;
	}
	public void setDtCriacaoFinal(String dtCriacaoFinal) {
		this.dtCriacaoFinal = dtCriacaoFinal;
	}
	public String getDtPublicacaoInicial() {
		return dtPublicacaoInicial;
	}
	public void setDtPublicacaoInicial(String dtPublicacaoInicial) {
		this.dtPublicacaoInicial = dtPublicacaoInicial;
	}
	public String getDtPublicacaoFinal() {
		return dtPublicacaoFinal;
	}
	public void setDtPublicacaoFinal(String dtPublicacaoFinal) {
		this.dtPublicacaoFinal = dtPublicacaoFinal;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
