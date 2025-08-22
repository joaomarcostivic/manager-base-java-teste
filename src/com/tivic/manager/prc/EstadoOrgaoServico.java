package com.tivic.manager.prc;

public class EstadoOrgaoServico {

	private int cdEstado;
	private int cdOrgao;
	private int cdProdutoServico;
	private Double vlServico;

	public EstadoOrgaoServico() { }

	public EstadoOrgaoServico(int cdEstado,
			int cdOrgao,
			int cdProdutoServico,
			Double vlServico) {
		setCdEstado(cdEstado);
		setCdOrgao(cdOrgao);
		setCdProdutoServico(cdProdutoServico);
		setVlServico(vlServico);
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
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
		valueToString += "cdEstado: " +  getCdEstado();
		valueToString += ", cdOrgao: " +  getCdOrgao();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", vlServico: " +  getVlServico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EstadoOrgaoServico(getCdEstado(),
			getCdOrgao(),
			getCdProdutoServico(),
			getVlServico());
	}

}