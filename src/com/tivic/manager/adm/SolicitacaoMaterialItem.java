package com.tivic.manager.adm;

public class SolicitacaoMaterialItem {

	private int cdProdutoServico;
	private int cdSolicitacaoMaterial;
	private int cdUnidadeMedida;
	private float qtSolicitada;

	public SolicitacaoMaterialItem(int cdProdutoServico,
			int cdSolicitacaoMaterial,
			int cdUnidadeMedida,
			float qtSolicitada){
		setCdProdutoServico(cdProdutoServico);
		setCdSolicitacaoMaterial(cdSolicitacaoMaterial);
		setCdUnidadeMedida(cdUnidadeMedida);
		setQtSolicitada(qtSolicitada);
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdSolicitacaoMaterial(int cdSolicitacaoMaterial){
		this.cdSolicitacaoMaterial=cdSolicitacaoMaterial;
	}
	public int getCdSolicitacaoMaterial(){
		return this.cdSolicitacaoMaterial;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public void setQtSolicitada(float qtSolicitada){
		this.qtSolicitada=qtSolicitada;
	}
	public float getQtSolicitada(){
		return this.qtSolicitada;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdSolicitacaoMaterial: " +  getCdSolicitacaoMaterial();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		valueToString += ", qtSolicitada: " +  getQtSolicitada();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SolicitacaoMaterialItem(getCdProdutoServico(),
			getCdSolicitacaoMaterial(),
			getCdUnidadeMedida(),
			getQtSolicitada());
	}

}
