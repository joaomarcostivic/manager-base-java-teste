package com.tivic.manager.grl;

public class AtributoGrupoProduto {

	private int cdProdutoAtributo;
	private int cdProdutoServico;
	private int cdEmpresa;
	private String txtAtributo;

	public AtributoGrupoProduto(int cdProdutoAtributo,
			int cdProdutoServico,
			int cdEmpresa,
			String txtAtributo){
		setCdProdutoAtributo(cdProdutoAtributo);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setTxtAtributo(txtAtributo);
	}
	public void setCdProdutoAtributo(int cdProdutoAtributo){
		this.cdProdutoAtributo=cdProdutoAtributo;
	}
	public int getCdProdutoAtributo(){
		return this.cdProdutoAtributo;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setTxtAtributo(String txtAtributo){
		this.txtAtributo=txtAtributo;
	}
	public String getTxtAtributo(){
		return this.txtAtributo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoAtributo: " +  getCdProdutoAtributo();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", txtAtributo: " +  getTxtAtributo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AtributoGrupoProduto(cdProdutoAtributo,
			cdProdutoServico,
			cdEmpresa,
			txtAtributo);
	}

}