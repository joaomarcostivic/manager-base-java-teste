package com.tivic.manager.prc;

public class CidadeOrgaoServico {

	private int cdOrgao;
	private int cdCidade;
	private int cdProdutoServico;
	private Double vlServico;

	public CidadeOrgaoServico() { }

	public CidadeOrgaoServico(int cdOrgao,
			int cdCidade,
			int cdProdutoServico,
			Double vlServico) {
		setCdOrgao(cdOrgao);
		setCdCidade(cdCidade);
		setCdProdutoServico(cdProdutoServico);
		setVlServico(vlServico);
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setVlServico(Double vlServico){
		this.vlServico=vlServico;
	}
	public Double getVlServico(){
		return this.vlServico;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrgao: " +  getCdOrgao();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", vlServico: " +  getVlServico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CidadeOrgaoServico(getCdOrgao(),
			getCdCidade(),
			getCdProdutoServico(),
			getVlServico());
	}

}