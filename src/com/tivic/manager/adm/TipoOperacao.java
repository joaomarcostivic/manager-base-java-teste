package com.tivic.manager.adm;

public class TipoOperacao {

	private int cdTipoOperacao;
	private String nmTipoOperacao;
	private String idTipoOperacao;
	private int stTipoOperacao;
	private int lgContrato;
	private int cdTabelaPreco;

	public TipoOperacao(int cdTipoOperacao,
			String nmTipoOperacao,
			String idTipoOperacao,
			int stTipoOperacao,
			int lgContrato,
			int cdTabelaPreco){
		setCdTipoOperacao(cdTipoOperacao);
		setNmTipoOperacao(nmTipoOperacao);
		setIdTipoOperacao(idTipoOperacao);
		setStTipoOperacao(stTipoOperacao);
		setLgContrato(lgContrato);
		setCdTabelaPreco(cdTabelaPreco);
	}
	public void setCdTipoOperacao(int cdTipoOperacao){
		this.cdTipoOperacao=cdTipoOperacao;
	}
	public int getCdTipoOperacao(){
		return this.cdTipoOperacao;
	}
	public void setNmTipoOperacao(String nmTipoOperacao){
		this.nmTipoOperacao=nmTipoOperacao;
	}
	public String getNmTipoOperacao(){
		return this.nmTipoOperacao;
	}
	public void setIdTipoOperacao(String idTipoOperacao){
		this.idTipoOperacao=idTipoOperacao;
	}
	public String getIdTipoOperacao(){
		return this.idTipoOperacao;
	}
	public void setStTipoOperacao(int stTipoOperacao){
		this.stTipoOperacao=stTipoOperacao;
	}
	public int getStTipoOperacao(){
		return this.stTipoOperacao;
	}
	public void setLgContrato(int lgContrato){
		this.lgContrato=lgContrato;
	}
	public int getLgContrato(){
		return this.lgContrato;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOperacao: " +  getCdTipoOperacao();
		valueToString += ", nmTipoOperacao: " +  getNmTipoOperacao();
		valueToString += ", idTipoOperacao: " +  getIdTipoOperacao();
		valueToString += ", stTipoOperacao: " +  getStTipoOperacao();
		valueToString += ", lgContrato: " +  getLgContrato();
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoOperacao(getCdTipoOperacao(),
			getNmTipoOperacao(),
			getIdTipoOperacao(),
			getStTipoOperacao(),
			getLgContrato(),
			getCdTabelaPreco());
	}

}