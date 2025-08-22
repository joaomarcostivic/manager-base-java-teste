package com.tivic.manager.mcr;

public class EmpreendimentoFornecedor {

	private int cdEmpreendimento;
	private int cdFornecedor;
	private String nmFornecedor;
	private int nrFrequenciaCompra;
	private String txtFidelidade;
	private String nmTipoProduto;

	public EmpreendimentoFornecedor(int cdEmpreendimento,
			int cdFornecedor,
			String nmFornecedor,
			int nrFrequenciaCompra,
			String txtFidelidade,
			String nmTipoProduto){
		setCdEmpreendimento(cdEmpreendimento);
		setCdFornecedor(cdFornecedor);
		setNmFornecedor(nmFornecedor);
		setNrFrequenciaCompra(nrFrequenciaCompra);
		setTxtFidelidade(txtFidelidade);
		setNmTipoProduto(nmTipoProduto);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
	}
	public void setNmFornecedor(String nmFornecedor){
		this.nmFornecedor=nmFornecedor;
	}
	public String getNmFornecedor(){
		return this.nmFornecedor;
	}
	public void setNrFrequenciaCompra(int nrFrequenciaCompra){
		this.nrFrequenciaCompra=nrFrequenciaCompra;
	}
	public int getNrFrequenciaCompra(){
		return this.nrFrequenciaCompra;
	}
	public void setTxtFidelidade(String txtFidelidade){
		this.txtFidelidade=txtFidelidade;
	}
	public String getTxtFidelidade(){
		return this.txtFidelidade;
	}
	public void setNmTipoProduto(String nmTipoProduto){
		this.nmTipoProduto=nmTipoProduto;
	}
	public String getNmTipoProduto(){
		return this.nmTipoProduto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", cdFornecedor: " +  getCdFornecedor();
		valueToString += ", nmFornecedor: " +  getNmFornecedor();
		valueToString += ", nrFrequenciaCompra: " +  getNrFrequenciaCompra();
		valueToString += ", txtFidelidade: " +  getTxtFidelidade();
		valueToString += ", nmTipoProduto: " +  getNmTipoProduto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EmpreendimentoFornecedor(cdEmpreendimento,
			cdFornecedor,
			nmFornecedor,
			nrFrequenciaCompra,
			txtFidelidade,
			nmTipoProduto);
	}

}