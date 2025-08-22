package com.tivic.manager.alm;

public class AcertoConsignacaoEntItem {

	private int cdAcertoConsignacao;
	private int cdEmpresa;
	private int cdProdutoServico;
	private float qtItem;
	private float vlItem;

	public AcertoConsignacaoEntItem(int cdAcertoConsignacao,
			int cdEmpresa,
			int cdProdutoServico,
			float qtItem,
			float vlItem){
		setCdAcertoConsignacao(cdAcertoConsignacao);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setQtItem(qtItem);
		setVlItem(vlItem);
	}
	public void setCdAcertoConsignacao(int cdAcertoConsignacao){
		this.cdAcertoConsignacao=cdAcertoConsignacao;
	}
	public int getCdAcertoConsignacao(){
		return this.cdAcertoConsignacao;
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
	public void setQtItem(float qtItem){
		this.qtItem=qtItem;
	}
	public float getQtItem(){
		return this.qtItem;
	}
	public void setVlItem(float vlItem){
		this.vlItem=vlItem;
	}
	public float getVlItem(){
		return this.vlItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAcertoConsignacao: " +  getCdAcertoConsignacao();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", qtItem: " +  getQtItem();
		valueToString += ", vlItem: " +  getVlItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AcertoConsignacaoEntItem(getCdAcertoConsignacao(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getQtItem(),
			getVlItem());
	}

}
