package com.tivic.manager.mob.templates;

public class RelatorioAitEstatisticaInfracoesNaiNip {
	private String data;
    private double localCidade;
    private double foraCidade;
    private double nInfracoes;
    private double nInfracoesTotal;
    private double mDiariaTotal;
    
    public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public double getLocalCidade() {
		return localCidade;
	}
	public void setLocalCidade(double localCidade) {
		this.localCidade = localCidade;
	}
	public double getForaCidade() {
		return foraCidade;
	}
	public void setForaCidade(double foraCidade) {
		this.foraCidade = foraCidade;
	}
	public double getnInfracoes() {
		return nInfracoes;
	}
	public void setnInfracoes(double nInfracoes) {
		this.nInfracoes = nInfracoes;
	}
	public double getnInfracoesTotal() {
		return nInfracoesTotal;
	}
	public void setnInfracoesTotal(double nInfracoesTotal) {
		this.nInfracoesTotal = nInfracoesTotal;
	}
	public double getmDiariaTotal() {
		return mDiariaTotal;
	}
	public void setmDiariaTotal(double mDiariaTotal) {
		this.mDiariaTotal = mDiariaTotal;
	}
}
