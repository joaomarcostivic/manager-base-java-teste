package com.tivic.manager.alm;

public class BalancoProdutoServico {

	private int cdBalanco;
	private int cdEmpresa;
	private int cdProdutoServico;
	private Double qtEstoque;
	private Double qtEstoqueBalanco;
	private int cdUnidadeMedida;

	public BalancoProdutoServico(int cdBalanco,
			int cdEmpresa,
			int cdProdutoServico,
			Double qtEstoque,
			Double qtEstoqueBalanco,
			int cdUnidadeMedida){
		setCdBalanco(cdBalanco);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setQtEstoque(qtEstoque);
		setQtEstoqueBalanco(qtEstoqueBalanco);
		setCdUnidadeMedida(cdUnidadeMedida);
	}
	public void setCdBalanco(int cdBalanco){
		this.cdBalanco=cdBalanco;
	}
	public int getCdBalanco(){
		return this.cdBalanco;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setQtEstoque(Double qtEstoque){
		this.qtEstoque=qtEstoque;
	}
	public Double getQtEstoque(){
		return this.qtEstoque;
	}
	public void setQtEstoqueBalanco(Double qtEstoqueBalanco){
		this.qtEstoqueBalanco=qtEstoqueBalanco;
	}
	public Double getQtEstoqueBalanco(){
		return this.qtEstoqueBalanco;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBalanco: " +  getCdBalanco();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", qtEstoque: " +  getQtEstoque();
		valueToString += ", qtEstoqueBalanco: " +  getQtEstoqueBalanco();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BalancoProdutoServico(getCdBalanco(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getQtEstoque(),
			getQtEstoqueBalanco(),
			getCdUnidadeMedida());
	}

}
