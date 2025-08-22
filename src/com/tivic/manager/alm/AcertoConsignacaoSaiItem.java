package com.tivic.manager.alm;

public class AcertoConsignacaoSaiItem {

	private int cdAcertoConsignacao;
	private int cdEmpresa;
	private int cdProdutoServico;
	private float vlItem;
	private float qtItemConsignado;
	private float qtItemNaoConsignado;

	public AcertoConsignacaoSaiItem(int cdAcertoConsignacao,
			int cdEmpresa,
			int cdProdutoServico,
			float vlItem,
			float qtItemConsignado,
			float qtItemNaoConsignado){
		setCdAcertoConsignacao(cdAcertoConsignacao);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setVlItem(vlItem);
		setQtItemConsignado(qtItemConsignado);
		setQtItemNaoConsignado(qtItemNaoConsignado);
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
	public void setVlItem(float vlItem){
		this.vlItem=vlItem;
	}
	public float getVlItem(){
		return this.vlItem;
	}
	public void setQtItemConsignado(float qtItemConsignado){
		this.qtItemConsignado=qtItemConsignado;
	}
	public float getQtItemConsignado(){
		return this.qtItemConsignado;
	}
	public void setQtItemNaoConsignado(float qtItemNaoConsignado){
		this.qtItemNaoConsignado=qtItemNaoConsignado;
	}
	public float getQtItemNaoConsignado(){
		return this.qtItemNaoConsignado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAcertoConsignacao: " +  getCdAcertoConsignacao();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", vlItem: " +  getVlItem();
		valueToString += ", qtItemConsignado: " +  getQtItemConsignado();
		valueToString += ", qtItemNaoConsignado: " +  getQtItemNaoConsignado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AcertoConsignacaoSaiItem(getCdAcertoConsignacao(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getVlItem(),
			getQtItemConsignado(),
			getQtItemNaoConsignado());
	}

}
