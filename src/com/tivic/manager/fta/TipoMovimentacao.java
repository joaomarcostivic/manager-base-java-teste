package com.tivic.manager.fta;

public class TipoMovimentacao {

	private int cdTipoMovimentacao;
	private String nmTipoMovimentacao;
	private int tpSituacaoPneu;

	public TipoMovimentacao(int cdTipoMovimentacao,
			String nmTipoMovimentacao,
			int tpSituacaoPneu){
		setCdTipoMovimentacao(cdTipoMovimentacao);
		setNmTipoMovimentacao(nmTipoMovimentacao);
		setTpSituacaoPneu(tpSituacaoPneu);
	}
	public void setCdTipoMovimentacao(int cdTipoMovimentacao){
		this.cdTipoMovimentacao=cdTipoMovimentacao;
	}
	public int getCdTipoMovimentacao(){
		return this.cdTipoMovimentacao;
	}
	public void setNmTipoMovimentacao(String nmTipoMovimentacao){
		this.nmTipoMovimentacao=nmTipoMovimentacao;
	}
	public String getNmTipoMovimentacao(){
		return this.nmTipoMovimentacao;
	}
	public void setTpSituacaoPneu(int tpSituacaoPneu){
		this.tpSituacaoPneu=tpSituacaoPneu;
	}
	public int getTpSituacaoPneu(){
		return this.tpSituacaoPneu;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoMovimentacao: " +  getCdTipoMovimentacao();
		valueToString += ", nmTipoMovimentacao: " +  getNmTipoMovimentacao();
		valueToString += ", tpSituacaoPneu: " +  getTpSituacaoPneu();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoMovimentacao(getCdTipoMovimentacao(),
			getNmTipoMovimentacao(),
			getTpSituacaoPneu());
	}

}