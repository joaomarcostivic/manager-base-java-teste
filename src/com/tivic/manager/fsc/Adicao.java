package com.tivic.manager.fsc;

public class Adicao {

	private int cdAdicao;
	private int cdDeclaracaoImportacao;
	private int cdPais;
	private int cdNcm;
	private float vlVmcv;
	private float qtPesoLiquido;
	private int nrAdicao;
	private int nrSeqAdicaoItem;
	private float vlDesconto;
	private String nrPedidoCompra;
	private String nrItemPedidoCompra;
	private float vlAduaneiro;
	
	public Adicao(int cdAdicao,
			int cdDeclaracaoImportacao,
			int cdPais,
			int cdNcm,
			float vlVmcv,
			float qtPesoLiquido,
			int nrAdicao,
			int nrSeqAdicaoItem,
			float vlDesconto,
			String nrPedidoCompra,
			String nrItemPedidoCompra,
			float vlAduaneiro){
		setCdAdicao(cdAdicao);
		setCdDeclaracaoImportacao(cdDeclaracaoImportacao);
		setCdPais(cdPais);
		setCdNcm(cdNcm);
		setVlVmcv(vlVmcv);
		setQtPesoLiquido(qtPesoLiquido);
		setNrAdicao(nrAdicao);
		setNrSeqAdicaoItem(nrSeqAdicaoItem);
		setVlDesconto(vlDesconto);
		setNrPedidoCompra(nrPedidoCompra);
		setNrItemPedidoCompra(nrItemPedidoCompra);
		setVlAduaneiro(vlAduaneiro);
	}
	public Adicao() {
	}
	public void setCdAdicao(int cdAdicao){
		this.cdAdicao=cdAdicao;
	}
	public int getCdAdicao(){
		return this.cdAdicao;
	}
	public void setCdDeclaracaoImportacao(int cdDeclaracaoImportacao){
		this.cdDeclaracaoImportacao=cdDeclaracaoImportacao;
	}
	public int getCdDeclaracaoImportacao(){
		return this.cdDeclaracaoImportacao;
	}
	public void setCdPais(int cdPais){
		this.cdPais=cdPais;
	}
	public int getCdPais(){
		return this.cdPais;
	}
	public void setCdNcm(int cdNcm){
		this.cdNcm=cdNcm;
	}
	public int getCdNcm(){
		return this.cdNcm;
	}
	public void setVlVmcv(float vlVmcv){
		this.vlVmcv=vlVmcv;
	}
	public float getVlVmcv(){
		return this.vlVmcv;
	}
	public void setQtPesoLiquido(float qtPesoLiquido){
		this.qtPesoLiquido=qtPesoLiquido;
	}
	public float getQtPesoLiquido(){
		return this.qtPesoLiquido;
	}
	public void setNrAdicao(int nrAdicao){
		this.nrAdicao=nrAdicao;
	}
	public int getNrAdicao(){
		return this.nrAdicao;
	}
	public void setNrSeqAdicaoItem(int nrSeqAdicaoItem){
		this.nrSeqAdicaoItem=nrSeqAdicaoItem;
	}
	public int getNrSeqAdicaoItem(){
		return this.nrSeqAdicaoItem;
	}
	public void setVlDesconto(float vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public float getVlDesconto(){
		return this.vlDesconto;
	}
	public void setNrPedidoCompra(String nrPedidoCompra){
		this.nrPedidoCompra=nrPedidoCompra;
	}
	public String getNrPedidoCompra(){
		return this.nrPedidoCompra;
	}
	public void setNrItemPedidoCompra(String nrItemPedidoCompra){
		this.nrItemPedidoCompra=nrItemPedidoCompra;
	}
	public String getNrItemPedidoCompra(){
		return this.nrItemPedidoCompra;
	}
	public void setVlAduaneiro(float vlAduaneiro) {
		this.vlAduaneiro = vlAduaneiro;
	}
	public float getVlAduaneiro() {
		return vlAduaneiro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAdicao: " +  getCdAdicao();
		valueToString += ", cdDeclaracaoImportacao: " +  getCdDeclaracaoImportacao();
		valueToString += ", cdPais: " +  getCdPais();
		valueToString += ", cdNcm: " +  getCdNcm();
		valueToString += ", vlVmcv: " +  getVlVmcv();
		valueToString += ", qtPesoLiquido: " +  getQtPesoLiquido();
		valueToString += ", nrAdicao: " +  getNrAdicao();
		valueToString += ", nrSeqAdicaoItem: " +  getNrSeqAdicaoItem();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", nrPedidoCompra: " +  getNrPedidoCompra();
		valueToString += ", nrItemPedidoCompra: " +  getNrItemPedidoCompra();
		valueToString += ", vlAduaneiro: " +  getVlAduaneiro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Adicao(getCdAdicao(),
			getCdDeclaracaoImportacao(),
			getCdPais(),
			getCdNcm(),
			getVlVmcv(),
			getQtPesoLiquido(),
			getNrAdicao(),
			getNrSeqAdicaoItem(),
			getVlDesconto(),
			getNrPedidoCompra(),
			getNrItemPedidoCompra(),
			getVlAduaneiro());
	}

}