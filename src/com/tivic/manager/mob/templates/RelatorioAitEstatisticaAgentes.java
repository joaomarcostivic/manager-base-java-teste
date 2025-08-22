package com.tivic.manager.mob.templates;

public class RelatorioAitEstatisticaAgentes {
	private String nome;
    private double mDiaria;
    private double mMensal;
    private double nInfracoesAgente;
    private double porcentagemInfracoes;
    private double totalInfracoesAgentes;
    private double mMensalTotal;
    private double mDiariaTotal;
    private double mMensalTotalAgente;
    private double mDiariaTotalAgente;
    private double qtdAgentes;
	
    public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getmDiaria() {
		return mDiaria;
	}
	public void setmDiaria(double mDiaria) {
		this.mDiaria = mDiaria;
	}
	public double getmMensal() {
		return mMensal;
	}
	public void setmMensal(double mMensal) {
		this.mMensal = mMensal;
	}
	public double getnInfracoesAgente() {
		return nInfracoesAgente;
	}
	public void setnInfracoesAgente(double nInfracoesAgente) {
		this.nInfracoesAgente = nInfracoesAgente;
	}
	public double getPorcentagemInfracoes() {
		return porcentagemInfracoes;
	}
	public void setPorcentagemInfracoes(double porcentagemInfracoes) {
		this.porcentagemInfracoes = porcentagemInfracoes;
	}
	public double getTotalInfracoesAgentes() {
		return totalInfracoesAgentes;
	}
	public void setTotalInfracoesAgentes(double totalInfracoesAgentes) {
		this.totalInfracoesAgentes = totalInfracoesAgentes;
	}
	public double getmMensalTotal() {
		return mMensalTotal;
	}
	public void setmMensalTotal(double mMensalTotal) {
		this.mMensalTotal = mMensalTotal;
	}
	public double getmDiariaTotal() {
		return mDiariaTotal;
	}
	public void setmDiariaTotal(double mDiariaTotal) {
		this.mDiariaTotal = mDiariaTotal;
	}
	public double getmMensalTotalAgente() {
		return mMensalTotalAgente;
	}
	public void setmMensalTotalAgente(double mMensalTotalAgente) {
		this.mMensalTotalAgente = mMensalTotalAgente;
	}
	public double getmDiariaTotalAgente() {
		return mDiariaTotalAgente;
	}
	public void setmDiariaTotalAgente(double mDiariaTotalAgente) {
		this.mDiariaTotalAgente = mDiariaTotalAgente;
	}
	
	public double getqtdAgentes() {
		return qtdAgentes;
	}
	
	public void setqtdAgentes(double qtdAgentes) {
		this.qtdAgentes = qtdAgentes;
	}
}
