package com.tivic.manager.mob.templates;

public class RelatorioAitEstatisticaGravidade {
	private String natureza;
    private double mDiaria;
    private double nInfracoesGravidade;
    private double porcentagemGravidade;
    private double totalInfracoes;
    private double mDiariaTotal;
    
    public String getNatureza() {
		return natureza;
	}
	public void setNatureza(String gravidade) {
		this.natureza = gravidade;
	}
	public double getmDiaria() {
		return mDiaria;
	}
	public void setmDiaria(double mDiaria) {
		this.mDiaria = mDiaria;
	}
	public double getnInfracoesGravidade() {
		return nInfracoesGravidade;
	}
	public void setnInfracoesGravidade(double nInfracoesGravidade) {
		this.nInfracoesGravidade = nInfracoesGravidade;
	}
	public double getPorcentagemGravidade() {
		return porcentagemGravidade;
	}
	public void setPorcentagemGravidade(double porcentagemGravidade) {
		this.porcentagemGravidade = porcentagemGravidade;
	}
	public double getTotalInfracoes() {
		return totalInfracoes;
	}
	public void setTotalInfracoes(double totalInfracoes) {
		this.totalInfracoes = totalInfracoes;
	}
	public double getmDiariaTotal() {
		return mDiariaTotal;
	}
	public void setmDiariaTotal(double mDiariaTotal) {
		this.mDiariaTotal = mDiariaTotal;
	}
}
