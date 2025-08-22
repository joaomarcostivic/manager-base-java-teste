package com.tivic.manager.grl;

public class ProdutoSimilar {

	private int cdProdutoServico;
	private int cdSimilar;
	private int lgReferenciado;
	private String nmProdutoSimilar;
	private String txtDescricao;
	private String txtEspecificacao;
	
	
	public ProdutoSimilar(int cdProdutoServico,
			int cdSimilar,
			int lgReferenciado){
		setCdProdutoServico(cdProdutoServico);
		setCdSimilar(cdSimilar);
		setLgReferenciado(lgReferenciado);
	}
	public ProdutoSimilar(int cdProdutoServico,
			int cdSimilar,
			int lgReferenciado,
			String nmProdutoSimilar,
			String txtDescricao,
			String txtEspecificacao){
		setCdProdutoServico(cdProdutoServico);
		setCdSimilar(cdSimilar);
		setLgReferenciado(lgReferenciado);
		setNmProdutoSimilar(nmProdutoSimilar);
		setTxtDescricao(txtDescricao);
		setTxtEspecificacao(txtEspecificacao);
		
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdSimilar(int cdSimilar){
		this.cdSimilar=cdSimilar;
	}
	public int getCdSimilar(){
		return this.cdSimilar;
	}
	public void setLgReferenciado(int lgReferenciado){
		this.lgReferenciado=lgReferenciado;
	}
	public int getLgReferenciado(){
		return this.lgReferenciado;
	}
	public void setNmProdutoSimilar(String nmProdutoSimilar) {
		this.nmProdutoSimilar = nmProdutoSimilar;
	}
	public String getNmProdutoSimilar() {
		return nmProdutoSimilar;
	}
	public void setTxtDescricao(String txtDescricao) {
		this.txtDescricao = txtDescricao;
	}
	public String getTxtDescricao() {
		return txtDescricao;
	}
	public void setTxtEspecificacao(String txtEspecificacao) {
		this.txtEspecificacao = txtEspecificacao;
	}
	public String getTxtEspecificacao() {
		return txtEspecificacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdSimilar: " +  getCdSimilar();
		valueToString += ", lgReferenciado: " +  getLgReferenciado();
		valueToString += ", nmProdutoSimilar: " +  getNmProdutoSimilar();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", txtEspecificacao: " +  getTxtEspecificacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoSimilar(getCdProdutoServico(),
			getCdSimilar(),
			getLgReferenciado(),
			getNmProdutoSimilar(),
			getTxtDescricao(),
			getTxtEspecificacao());
	}

}
