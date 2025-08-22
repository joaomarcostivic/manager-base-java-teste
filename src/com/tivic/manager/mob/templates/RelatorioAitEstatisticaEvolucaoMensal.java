package com.tivic.manager.mob.templates;

public class RelatorioAitEstatisticaEvolucaoMensal {
	
	private double qtd;
    private double ano;
    private double porcentagemInfracaoMes;
    private double mediaDiariaMes;
    private double mes;
    private double mMensalTotal;
    private double totalInfracoes;
    private double mDiariaTotal;

    public double getQtd() {
		return qtd;
	}
	public void setQtd(double qtd) {
		this.qtd = qtd;
	}
	public double getAno() {
		return ano;
	}
	public void setAno(double ano) {
		this.ano = ano;
	}
	public double getPorcentagemInfracaoMes() {
		return porcentagemInfracaoMes;
	}
	public void setPorcentagemInfracaoMes(double porcentagemInfracaoMes) {
		this.porcentagemInfracaoMes = porcentagemInfracaoMes;
	}
	public double getMediaDiariaMes() {
		return mediaDiariaMes;
	}
	public void setMediaDiariaMes(double mediaDiariaMes) {
		this.mediaDiariaMes = mediaDiariaMes;
	}
	public double getMes() {
		return mes;
	}
	public void setMes(double mes) {
		this.mes = mes;
	}
	public double getMMensalTotal() {
		return mMensalTotal;
	}
	public void setMMensalTotal(double m_mensalTotal) {
		this.mMensalTotal = m_mensalTotal;
	}
	public double getTotalInfracoes() {
		return totalInfracoes;
	}
	public void setTotalInfracoes(double total_infracoes) {
		this.totalInfracoes = total_infracoes;
	}
	public double getMDiariaTotal() {
		return mDiariaTotal;
	}
	public void setMDiariaTotal(double m_diariaTotal) {
		this.mDiariaTotal = m_diariaTotal;
	}
	
}
